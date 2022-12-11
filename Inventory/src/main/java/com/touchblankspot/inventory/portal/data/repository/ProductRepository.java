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

  @Query(
      value =
          """
          select
              product.id,product.short_name as shortName,
              product.name,product.short_description as shortDescription,
              product.description,product.material,
              product.discount_percentage as discountPercentage,
              product.max_discount_amount as maxDiscountAmount,
              product_category.category as categoryName,
              product_category.sub_category as subCategory,
              product_category.product_size as productSize
          from product inner join product_category on product.category_id = product_category.id
          where product.is_deleted = false ORDER BY product.id
          """,
      countQuery = "select count(*) from product where is_deleted = false",
      nativeQuery = true)
  Page<Object[]> getListData(Pageable pageable);
}
