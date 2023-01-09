package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
  Product findByNameAndIsDeleted(String name, Boolean isDeleted);

  List<Product> findByCategoryIdAndIsDeleted(UUID id, Boolean isDeleted);

  Product findByShortNameAndIsDeleted(String shortName, Boolean isDeleted);

  Product findByIdAndIsDeleted(UUID id, Boolean isDeleted);

  @Query(
          value =
                  """
                  select
                  *
                  from
                    (
                    select
                        product.id,product.short_name as shortName,product.name,product.short_description as shortDescription,
                        product.material,category.category as categoryName,category.sub_category as subCategory
                    from product inner join category on product.category_id = category.id
                    where
                      (
                        (:searchType = 'sname' and product.short_name like %:searchKey%) or
                        (:searchType = 'name' and product.name like %:searchKey%) or
                        (:searchType = 'mat' and product.material like %:searchKey%) or
                        (:searchType = 'cat' and category.category like %:searchKey%) or
                        (:searchType = 'subcat' and category.sub_category like %:searchKey%) or
                        (:searchType = '' and 1=1)
                      ) and
                      product.is_deleted = false
                    )R1
                  """,
          countQuery = "select count(*) from product where is_deleted = false",
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
                                when :searchType = 'mat' then product.material
                                when :searchType = 'cat' then category.category
                                when :searchType = 'subcat' then category.sub_category
                          end as result
                        from
                          product inner join category on product.category_id = category.id
                        where
                        (
                          (:searchType = 'sname' and product.short_name like %:searchKey%) or
                          (:searchType = 'name' and product.name like %:searchKey%) or
                          (:searchType = 'mat' and product.material like %:searchKey%) or
                          (:searchType = 'cat' and category.category like %:searchKey%) or
                          (:searchType = 'subcat' and category.sub_category like %:searchKey%)
                        ) and
                        product.is_deleted = false
                      )suggestions
                   group by result
                   order by result
                """)
  List<String> getAutoCompleteSuggestions(
          @Param("searchType") String searchType, @Param("searchKey") String searchKey);
}
