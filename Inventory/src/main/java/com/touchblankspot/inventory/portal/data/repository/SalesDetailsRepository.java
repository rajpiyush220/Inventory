package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.SalesDetails;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesDetailsRepository extends JpaRepository<SalesDetails, UUID> {

  @Query(
      nativeQuery = true,
      value =
          """
            select * from
            (
              select
                sales_details.id,product.name,product.short_name,product.short_description,category.category,
                category.sub_category,sales_details.quantity,sales_details.unit_price,
                case
                  when sales_details.discount_amount > 0 then sales_details.discount_amount
                  else 'No Discount'
                end as discount_amount,
                sales_details.total_price,sales_details.payment_mode,sales_details.transaction_id,
                sales_details.sold_by,DATE_FORMAT(sales_details.sold_at, '%d-%m-%Y %h:%i:%S %p') as soldAt
              from sales_details
                inner join product on product.id = sales_details.product_id
                inner join category on category.id = product.category_id
            )R1
            where
              (
                (:searchType = 'name' and name like %:searchKey%) or
                (:searchType = 'sname' and short_name like %:searchKey%) or
                (:searchType = 'sdes' and short_description like %:searchKey%) or
                (:searchType = 'cat' and category like %:searchKey%) or
                (:searchType = 'subcat' and sub_category like %:searchKey%) or
                (:searchType = 'qty' and quantity like %:searchKey%) or
                (:searchType = 'uprice' and unit_price like %:searchKey%) or
                (:searchType = 'totprice' and total_price like %:searchKey%) or
                (:searchType = 'payMode' and payment_mode like %:searchKey%) or
                (:searchType = 'tranid' and transaction_id like %:searchKey%) or
                (:searchType = 'opt' and sold_by like %:searchKey%) or
                (:searchType = '' and 1=1)
              )
          """)
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
                              when :searchType = 'name' then product.name
                              when :searchType = 'sname' then product.short_name
                              when :searchType = 'sdes' then product.short_description
                              when :searchType = 'cat' then category.category
                              when :searchType = 'subcat' then category.sub_category
                              when :searchType = 'qty' then sales_details.quantity
                              when :searchType = 'uprice' then sales_details.unit_price
                              when :searchType = 'totprice' then sales_details.total_price
                              when :searchType = 'payMode' then sales_details.payment_mode
                              when :searchType = 'tranid' then sales_details.transaction_id
                              when :searchType = 'opt' then sales_details.sold_by
                          end as result
                        from sales_details
                          inner join product on product.id = sales_details.product_id
                          inner join category on category.id = product.category_id
                        where
                          (
                            (:searchType = 'name' and product.name like %:searchKey%) or
                            (:searchType = 'sname' and product.short_name like %:searchKey%) or
                            (:searchType = 'sdes' and product.short_description like %:searchKey%) or
                            (:searchType = 'cat' and category.category like %:searchKey%) or
                            (:searchType = 'subcat' and category.sub_category like %:searchKey%) or
                            (:searchType = 'qty' and sales_details.quantity like %:searchKey%) or
                            (:searchType = 'uprice' and sales_details.unit_price like %:searchKey%) or
                            (:searchType = 'totprice' and sales_details.total_price like %:searchKey%) or
                            (:searchType = 'payMode' and sales_details.payment_mode like %:searchKey%) or
                            (:searchType = 'tranid' and sales_details.transaction_id like %:searchKey%) or
                            (:searchType = 'opt' and sales_details.sold_by like %:searchKey%)
                          )
                      )suggestions
                   group by result
                   order by result
                """)
  List<String> getAutoCompleteSuggestions(
      @Param("searchType") String searchType, @Param("searchKey") String searchKey);
}
