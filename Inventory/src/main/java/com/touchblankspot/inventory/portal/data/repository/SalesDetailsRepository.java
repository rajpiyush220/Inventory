package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.SalesDetails;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesDetailsRepository extends JpaRepository<SalesDetails, UUID> {

  @Query(
      nativeQuery = true,
      value =
          """
            select
              sales_details.id,product.name,product.short_name,product.short_description,category.category,
              category.sub_category,sales_details.quantity,sales_details.unit_price,
              case
                when sales_details.discount_amount > 0 then sales_details.discount_amount
                else 'No Discount'
              end as discount_amount,
              sales_details.total_price,sales_details.payment_mode,sales_details.transaction_id,
              sales_details.sold_by,DATE_FORMAT(sales_details.sold_at, '%D %M %Y %h:%i:%S %p') as soldAt
            from sales_details
              inner join product on product.id = sales_details.product_id
              inner join category on category.id = product.category_id
            order by sales_details.created, sales_details.quantity
          """)
  Page<Object[]> getListData(Pageable pageable);
}
