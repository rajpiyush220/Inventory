package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.Stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

  Optional<Stock> findByProductIdAndProductSize(UUID productId, String size);

  @Query(
      value =
          """
            select
              *
            from
            (
              select
                uuid_to_bin(uuid()) as id,product.name, product.short_name as shortName,
                product.short_description as shortDescription,
                coalesce(product_price.product_size,'N/A') as productSize,
                category.category as categoryName,category.sub_category as subCategory,
                coalesce(stock.quantity,0) as quantity
              from product
                inner join category on category.id = product.category_id
                left join product_price on product.id = product_price.product_id
                left join stock 
                  on 
                    stock.product_id = product_price.product_id and 
                    product_price.product_size = stock.product_size
            )R1
            where
                (
                  (:searchType = 'sname' and shortName like %:searchKey%) or
                  (:searchType = 'name' and name like %:searchKey%) or
                  (:searchType = 'shortd' and shortDescription like %:searchKey%) or
                  (:searchType = 'psize' and productSize like %:searchKey%) or
                  (:searchType = 'cat' and categoryName like %:searchKey%) or
                  (:searchType = 'subcat' and subCategory like %:searchKey%) or
                  (:searchType = 'quan' and quantity like %:searchKey%) or
                  (:searchType = '' and 1=1)
                )
          """,
      countQuery = "select count(*) from stock",
      nativeQuery = true)
  Page<Object[]> getListData(Pageable pageable, String searchType, String searchKey);

  @Query(
          nativeQuery = true,
          value =
                  """
                   select
                      result
                   from
                      (
                        select
                          case
                              when :searchType = 'sname' then product.short_name
                              when :searchType = 'name' then product.name
                              when :searchType = 'shortd' then product.short_description
                              when :searchType = 'psize' then coalesce(product_price.product_size,'N/A')
                              when :searchType = 'cat' then category.category
                              when :searchType = 'subcat' then category.sub_category
                              when :searchType = 'quan' then coalesce(stock.quantity,0)
                          end as result
                        from product
                          inner join category on category.id = product.category_id
                          left join product_price on product.id = product_price.product_id
                          left join stock 
                            on 
                              stock.product_id = product_price.product_id and 
                              product_price.product_size = stock.product_size
                        where
                          (
                            (:searchType = 'sname' and product.short_name like %:searchKey%) or
                            (:searchType = 'name' and product.name like %:searchKey%) or
                            (:searchType = 'shortd' and product.short_description like %:searchKey%) or
                            (:searchType = 'psize' and coalesce(product_price.product_size,'N/A') like %:searchKey%) or
                            (:searchType = 'cat' and category.category like %:searchKey%) or
                            (:searchType = 'subcat' and category.sub_category like %:searchKey%) or
                            (:searchType = 'quan' and coalesce(stock.quantity,0) like %:searchKey%)
                          )
                      )suggestions
                   group by result
                   order by result
                """)
  List<String> getAutoCompleteSuggestions(
          @Param("searchType") String searchType, @Param("searchKey") String searchKey);
}
