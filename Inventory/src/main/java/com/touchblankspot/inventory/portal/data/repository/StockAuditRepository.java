package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.StockAudit;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAuditRepository extends JpaRepository<StockAudit, UUID> {

  @Query(
      value =
          """
            select * from
            (
            select
                stock_audit.id,product.name,product.short_name as shortName,product.short_description as shortDescription,
                stock_audit.operation_type, stock_audit.quantity , stock_audit.operated_by,
                DATE_FORMAT(stock_audit.transaction_time, '%d-%m-%Y %h:%i:%S %p') as transaction_time
            from
                stock_audit inner join stock on stock.id = stock_audit.stock_id
                inner join product on product.id = stock.product_id
                inner join category on category.id = product.category_id
            )R1
            where
                (
                  (:searchType = 'sname' and shortName like %:searchKey%) or
                  (:searchType = 'name' and name like %:searchKey%) or
                  (:searchType = 'sdes' and shortDescription like %:searchKey%) or
                  (:searchType = 'otype' and operation_type like %:searchKey%) or
                  (:searchType = 'qty' and quantity like %:searchKey%) or
                  (:searchType = 'opt' and operated_by like %:searchKey%) or
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
                              when :searchType = 'sdes' then product.short_description
                              when :searchType = 'otype' then stock_audit.operation_type
                              when :searchType = 'qty' then stock_audit.quantity
                              when :searchType = 'opt' then stock_audit.operated_by
                          end as result
                        from
                            stock_audit inner join stock on stock.id = stock_audit.stock_id
                            inner join product on product.id = stock.product_id
                            inner join category on category.id = product.category_id
                        where
                          (
                            (:searchType = 'sname' and product.short_name like %:searchKey%) or
                            (:searchType = 'name' and product.name like %:searchKey%) or
                            (:searchType = 'sdes' and product.short_description like %:searchKey%) or
                            (:searchType = 'otype' and stock_audit.operation_type like %:searchKey%) or
                            (:searchType = 'qty' and stock_audit.quantity like %:searchKey%) or
                            (:searchType = 'opt' and stock_audit.operated_by like %:searchKey%)
                          )
                      )suggestions
                   group by result
                   order by result
                """)
  List<String> getAutoCompleteSuggestions(
      @Param("searchType") String searchType, @Param("searchKey") String searchKey);
}
