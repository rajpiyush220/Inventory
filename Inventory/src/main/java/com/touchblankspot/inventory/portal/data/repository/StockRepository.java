package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.Stock;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

  @Query(
      value =
          """
            select
              stock.id,product.name,product.short_name as shortName,product.short_description as shortDescription,
              product_category.product_size as productSize,product_category.category as categoryName,
              product_category.sub_category as subCategory,stock.quantity
            from
              stock inner join product on product.id = stock.product_id
                inner join category on category.id = product.category_id
                left join product_price on product_price.product_id = product.id
          """,
      countQuery = "select count(*) from stock",
      nativeQuery = true)
  Page<Object[]> getListData(Pageable pageable);
}
