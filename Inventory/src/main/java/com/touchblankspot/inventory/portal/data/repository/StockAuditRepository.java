package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.StockAudit;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAuditRepository extends JpaRepository<StockAudit, UUID> {

  @Query(
      value =
          """
            select
                stock_audit.id,product.name,product.short_name as shortName,product.short_description as shortDescription,
                stock_audit.operation_type, stock_audit.quantity , stock_audit.operated_by,
                DATE_FORMAT(stock_audit.transaction_time, '%D %M %Y %h:%i:%S %p') as transaction_time
            from
                stock_audit inner join stock on stock.id = stock_audit.stock_id
                inner join product on product.id = stock.product_id
                inner join category on category.id = product.category_id
            order by stock_audit.created desc
          """,
      countQuery = "select count(*) from stock",
      nativeQuery = true)
  Page<Object[]> getListData(Pageable pageable);
}
