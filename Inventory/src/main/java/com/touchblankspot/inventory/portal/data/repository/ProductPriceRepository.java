package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.ProductPrice;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {

  Optional<ProductPrice> findByProductIdAndProductSize(UUID productId, String productSize);

  @Query(
      value =
          """
            select
              product_price.id,product.short_name,product.name, product.short_description, product.material,
              product_price.product_size,product_price.price,
              coalesce(product_price.discount_percentage, '') as discount_percentage,
              coalesce(product_price.max_discount_amount,'') as max_discount_amount
            from product_price inner join product on product.id = product_price.product_id
            order by product.name,product_price.product_size
              """,
      countQuery = "select count(*) from product_price",
      nativeQuery = true)
  Page<Object[]> getListData(Pageable pageable);
}
