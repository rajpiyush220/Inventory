package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.data.model.Category;
import com.touchblankspot.inventory.portal.service.CategoryService;
import com.touchblankspot.inventory.portal.web.annotations.ProductController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.AutoCompleteWrapper;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.CategoryMapper;
import com.touchblankspot.inventory.portal.web.types.product.category.CategoryRequestType;
import com.touchblankspot.inventory.portal.web.types.product.category.CategoryResponseType;
import com.touchblankspot.inventory.portal.web.types.product.category.CategoryUpdateRequestType;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@ProductController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CategoryController extends BaseController {

  @NonNull private final CategoryService categoryService;

  @NonNull private final CategoryMapper categoryMapper;

  private static final List<SelectType> SEARCH_TYPES =
      List.of(
          new SelectType("category", "Category"), new SelectType("subcategory", "Sub Category"));

  @GetMapping("/categories")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  public String getAll(
      Model model,
      @RequestParam(value = "page", defaultValue = "1", required = false) Integer currentPage,
      @RequestParam(value = "size", defaultValue = "0", required = false) Integer pageSize,
      @RequestParam(value = "sortColumn", defaultValue = "category", required = false)
          String sortColumn,
      @RequestParam(value = "sortOrder", defaultValue = "ASC", required = false) String sortOrder,
      @RequestParam(value = "searchKey", defaultValue = "", required = false) String searchKey,
      @RequestParam(value = "searchType", defaultValue = "", required = false) String searchType) {

    pageSize = pageSize > 0 ? pageSize : pageSizeList.get(0);
    sortOrder = sortOrder.toUpperCase();
    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortColumn);
    Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
    Page<Category> productCategoryPage = categoryService.findAll(pageable, searchType, searchKey);
    List<CategoryResponseType> responseTypeList =
        productCategoryPage.stream().map(categoryMapper::toResponse).toList();
    int totalPages = productCategoryPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers =
          IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("ProductCategoriesPage", productCategoryPage);
    model.addAttribute("ProductCategories", responseTypeList);
    model.addAttribute("currentPageNumber", currentPage);
    model.addAttribute("PageSizeList", pageSizeList);
    model.addAttribute("selectedPageSize", pageSize);
    model.addAttribute("sortOrder", sortOrder);
    model.addAttribute("sortColumn", sortColumn);
    model.addAttribute("currentPageSize", pageSize);
    model.addAttribute("searchType", searchType);
    model.addAttribute("searchKey", searchKey);
    model.addAttribute("searchTypes", SEARCH_TYPES);

    return "product/category/show";
  }

  @GetMapping("/category")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_CREATE'})")
  public String createProductCategory(Model model) {
    model.addAttribute("categoryForm", new CategoryRequestType());
    model.addAttribute("existingCategories", categoryService.getCategoryList());
    return "product/category/create";
  }

  @PostMapping("/category")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_CREATE'})")
  public String createProductCategory(
      @Valid @ModelAttribute("categoryForm") CategoryRequestType requestType,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("existingCategories", categoryService.getCategoryList());
      if (!ObjectUtils.isEmpty(requestType.getExistingCategories())) {
        model.addAttribute("selectedCategory", requestType.getExistingCategories());
        model.addAttribute(
            "existingSubCategories",
            categoryService.getSubCategorySelectList(requestType.getExistingCategories()));
        model.addAttribute("selectedSubCategory", requestType.getExistingCategories());
      }
      return "product/category/create";
    }
    try {
      categoryService.save(categoryMapper.toEntity(requestType));
      model.addAttribute("successMessage", "Product category Created successfully.");
    } catch (Exception ex) {
      log.error("Unable to create category", ex);
      model.addAttribute(
          "errorMessage", "Unable to create Product category. please contact administrator");
    }
    return "redirect:/product/category";
  }

  @GetMapping(value = "/category/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  @ResponseBody
  public AutoCompleteWrapper getSearchSuggestion(String searchKey, String type) {
    List<String> results = categoryService.getAutoCompleteSuggestions(type, searchKey);
    return new AutoCompleteWrapper(results);
  }

  @GetMapping("/category/delete")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_DELETE'})")
  public String deleteCategory(String id) {
    categoryService.deleteProductCategory(UUID.fromString(id));
    log.warn("Product category with id {} deleted successfully.", id);
    return "redirect:/product/categories";
  }

  @GetMapping("/category/edit")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_UPDATE'})")
  public String editCategory(String id, Model model) {
    Category category = categoryService.findById(UUID.fromString(id));
    model.addAttribute(
        "categoryForm",
        CategoryUpdateRequestType.builder()
            .id(category.getId())
            .category(category.getCategory())
            .subCategory(category.getSubCategory())
            .build());
    return "product/category/edit";
  }

  @PostMapping("/category/update")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_UPDATE'})")
  public String updateCategory(
      @Valid @ModelAttribute("categoryForm") CategoryUpdateRequestType updateRequestType,
      BindingResult bindingResult,
      Model model) {
    model.addAttribute("categoryForm", updateRequestType);
    if (!bindingResult.hasErrors()) {
      try {
        categoryService.updateCategory(updateRequestType);
        log.warn("Product category with id {} deleted successfully.", updateRequestType.getId());
        model.addAttribute("successMessage", "Category updated Successfully");
      } catch (Exception ex) {
        log.error("Unable to update category", ex);
        model.addAttribute("errorMessage", "Unable to update Category. Please contact Admin.");
      }
    }
    return "product/category/edit";
  }

  @GetMapping("/category/view")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  public String ViewCategory(String id, Model model) {
    model.addAttribute(
        "category", categoryMapper.toResponse(categoryService.findById(UUID.fromString(id))));
    return "product/category/view";
  }

  @GetMapping(
      value = "/category/subCategories",
      produces = "application/json",
      consumes = "application/json")
  @ResponseBody
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  public ResponseEntity<List<SelectType>> getProductSubCategories(
      @RequestParam String categoryName) {
    return ResponseEntity.ok(categoryService.getSubCategorySelectList(categoryName));
  }
}
