package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.ProductPrice;
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
public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {

  Optional<ProductPrice> findByProductIdAndProductSize(UUID productId, String productSize);

  List<ProductPrice> findByProductId(UUID productId);

  @Query(
      value =
          """
            select
            *
            from
            (
                select
                  product_price.id,product.short_name,product.name, product.short_description, product.material,
                  product_price.product_size,product_price.price,
                  coalesce(product_price.discount_percentage, '') as discount_percentage,
                  coalesce(product_price.max_discount_amount,'') as max_discount_amount
                from product_price inner join product on product.id = product_price.product_id
            )R1
            where
                (
                  (:searchType = 'sname' and short_name like %:searchKey%) or
                  (:searchType = 'name' and name like %:searchKey%) or
                  (:searchType = 'shortd' and short_description like %:searchKey%) or
                  (:searchType = 'mat' and material like %:searchKey%) or
                  (:searchType = 'psize' and product_size like %:searchKey%) or
                  (:searchType = 'pprice' and price like %:searchKey%) or
                  (:searchType = 'disper' and discount_percentage like %:searchKey%) or
                  (:searchType = 'maxdisamt' and max_discount_amount like %:searchKey%) or
                  (:searchType = '' and 1=1)
                )
              """,
      countQuery = "select count(*) from product_price",
      nativeQuery = true)
  Page<Object[]> getListData(
      Pageable pageable,
      @Param("searchType") String searchType,
      @Param("searchKey") String searchKey);

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
                              when :searchType = 'mat' then product.material
                              when :searchType = 'psize' then product_price.product_size
                              when :searchType = 'pprice' then product_price.price
                              when :searchType = 'disper' then coalesce(product_price.discount_percentage, '')
                              when :searchType = 'maxdisamt' then coalesce(product_price.max_discount_amount,'')
                          end as result
                        from
                          product_price inner join product on product.id = product_price.product_id
                        where
                        (
                          (:searchType = 'sname' and short_name like %:searchKey%) or
                          (:searchType = 'name' and name like %:searchKey%) or
                          (:searchType = 'shortd' and short_description like %:searchKey%) or
                          (:searchType = 'mat' and material like %:searchKey%) or
                          (:searchType = 'psize' and product_size like %:searchKey%) or
                          (:searchType = 'pprice' and price like %:searchKey%) or
                          (:searchType = 'disper' and discount_percentage like %:searchKey%) or
                          (:searchType = 'maxdisamt' and max_discount_amount like %:searchKey%)
                        )
                      )suggestions
                   group by result
                   order by result
                """)
  List<String> getAutoCompleteSuggestions(
      @Param("searchType") String searchType, @Param("searchKey") String searchKey);

  @Query(
      nativeQuery = true,
      value =
          """
        select
            product_price.id as id,category.category,category.sub_category,product.name,product_price.product_size,
            product_price.price,product_price.discount_percentage,coalesce(product_price.max_discount_amount,'')
        from
            product_price inner join product on product.id = product_price.product_id
            inner join category on category.id = product.category_id
        where product_price.id = :id group by product_price.id limit 1
      """)
  Object[] getPriceViewData(@Param("id") UUID id);
}
