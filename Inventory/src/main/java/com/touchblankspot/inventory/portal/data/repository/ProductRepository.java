package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
  Product findByNameAndIsDeleted(String name, Boolean isDeleted);

  Product findByShortNameAndIsDeleted(String shortName, Boolean isDeleted);

  Product findByIdAndIsDeleted(UUID id, Boolean isDeleted);

  @Query(
      value =
          """
          select
              product.id,product.short_name as shortName,
              product.name,product.short_description as shortDescription,
              product.description,product.material,
              category.category as categoryName,
              category.sub_category as subCategory
          from product inner join category on product.category_id = category.id
          where product.is_deleted = false ORDER BY product.id
          """,
      countQuery = "select count(*) from product where is_deleted = false",
      nativeQuery = true)
  Page<Object[]> getListData(Pageable pageable);
}
