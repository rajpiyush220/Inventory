package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.Category;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

  List<Category> findByCategoryAndSubCategoryAndIsDeleted(
      String category, String subCategory, Boolean isDeleted);

  List<Category> findAllByIsDeleted(Boolean isDeleted);

  List<Category> findAllByCategoryAndIsDeleted(String category, Boolean isDeleted);

  Category findByIdAndIsDeleted(UUID id, Boolean isDeleted);

  @Query(
      value =
          """
                    select
                        *
                    from category
                    where
                      (
                        (:searchType = 'category' and category like %:searchKey%) or
                        (:searchType = 'subcategory' and sub_category like %:searchKey%) or
                        (:searchType = '' and 1=1)
                      ) and
                      is_deleted = false
                    """,
      countQuery = "select count(*) from category where is_deleted = false",
      nativeQuery = true)
  Page<Category> findAll(
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
                                      when :searchType = 'category' then category
                                      else sub_category
                                    end as result
                                  from category
                                  where
                                    (
                                      (:searchType = 'category' and category like %:searchKey%) or
                                      (:searchType = 'subcategory' and sub_category like %:searchKey%)
                                    ) and
                                    is_deleted = false
                                  )suggestions
                               group by result
                               order by result
                            """)
  List<String> getAutoCompleteSuggestions(
      @Param("searchType") String searchType, @Param("searchKey") String searchKey);
}
