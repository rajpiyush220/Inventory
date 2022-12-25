package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.Stock;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

  Optional<Stock> findByProductIdAndProductSize(UUID productId, String size);

  @Query(
      value =
          """
            select
              uuid_to_bin(uuid()) as id,product.name, product.short_name as shortName,
              product.short_description as shortDescription,
              coalesce(product_price.product_size,'N/A') as productSize,
              category.category as categoryName,category.sub_category as subCategory,
              coalesce(stock.quantity,0) as quantity
            from product
              inner join category on category.id = product.category_id
              left join product_price on product.id = product_price.product_id
              left join stock on stock.product_id = product_price.product_id and product_price.product_size = stock.product_size
            order by product.name,product_price.product_size ,stock.quantity
          """,
      countQuery = "select count(*) from stock",
      nativeQuery = true)
  Page<Object[]> getListData(Pageable pageable);
}
