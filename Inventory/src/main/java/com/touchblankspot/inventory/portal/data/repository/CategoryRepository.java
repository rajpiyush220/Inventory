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

  List<Category> findByCategoryAndIsDeleted(String category, Boolean isDeleted);

  List<Category> findByCategoryAndSubCategoryAndIsDeleted(
      String category, String subCategory, Boolean isDeleted);

  List<Category> findBySubCategoryAndIsDeleted(String subCategory, Boolean isDeleted);

  List<Category> findAllByIsDeleted(Boolean isDeleted);

  List<Category> findAllByCategoryAndIsDeleted(String category, Boolean isDeleted);

  Category findByIdAndIsDeleted(UUID id, Boolean isDeleted);

  @Query(
      value = "select * from category where is_deleted = false ORDER BY id",
      countQuery = "select count(*) from category where is_deleted = false",
      nativeQuery = true)
  Page<Category> findAll(Pageable pageable);

  @Query(
      nativeQuery = true,
      value =
          """
              select
                category
              from category
              where
                category like %:category% and
                is_deleted = false
              group by category
              order by category
              """)
  List<String> findByCategoryContains(@Param("category") String category);

  @Query(
      nativeQuery = true,
      value =
          """
              select
                product_size
              from category
              where
                product_size like '%:productSize%' and
                is_deleted = false
              group by product_size
              order by product_size
              """)
  List<String> findByProductSizeContains(@Param("productSize") String productSize);

  @Query(
      nativeQuery = true,
      value =
          """
              select
                sub_category
              from sub_category
              where
                sub_category like '%:subCategory%' and
                is_deleted = false
              group by sub_category
              order by sub_category
              """)
  List<String> findBySubCategoryContains(@Param("subCategory") String subCategory);
}
