package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.data.model.Category;
import com.touchblankspot.inventory.portal.service.CategoryService;
import com.touchblankspot.inventory.portal.web.annotations.ProductController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.AutoCompleteWrapper;
import com.touchblankspot.inventory.portal.web.types.mapper.CategoryMapper;
import com.touchblankspot.inventory.portal.web.types.product.category.CategoryRequestType;
import com.touchblankspot.inventory.portal.web.types.product.category.CategoryResponseType;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
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

  @NonNull private final CategoryService productCategoryService;

  @NonNull private final CategoryMapper categoryMapper;

  @GetMapping("/categories")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  public String getAll(
      Model model,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(pageSizeList.get(0));

    Page<Category> productCategoryPage =
        productCategoryService.findAll(PageRequest.of(currentPage - 1, pageSize));
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
    model.addAttribute("selectedPageSize", pageSizeList.get(0));
    return "product/category/show";
  }

  @GetMapping("/category")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_CREATE'})")
  public String createProductCategory(Model model) {
    model.addAttribute("categoryForm", new CategoryRequestType());
    return "product/category/create";
  }

  @PostMapping("/category")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_CREATE'})")
  public String createProductCategory(
      @Valid @ModelAttribute("categoryForm") CategoryRequestType requestType,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return "product/category/create";
    }
    try {
      productCategoryService.save(categoryMapper.toEntity(requestType));
      model.addAttribute("successMessage", "Product category Created successfully.");
    } catch (Exception ex) {
      log.error("Unable to create category", ex);
      model.addAttribute(
          "errorMessage", "Unable to create Product category. please contact administrator");
    }
    return "product/category/create";
  }

  @GetMapping(value = "/category/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  @ResponseBody
  public AutoCompleteWrapper getSearchSuggestion(String searchKey, String type) {
    return new AutoCompleteWrapper(
        switch (type.toLowerCase()) {
          case "category" -> productCategoryService.findByCategoryContains(searchKey);
          case "subcategory" -> productCategoryService.findBySubCategoryContains(searchKey);
          case "productsize" -> productCategoryService.findByProductSizeContains(searchKey);
          default -> new ArrayList<>();
        });
  }

  @GetMapping("/category/delete")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_DELETE'})")
  public String deleteCategory(String id) {
    productCategoryService.deleteProductCategory(UUID.fromString(id));
    log.warn("Product category with id {} deleted successfully.", id);
    return "redirect:/product/categories";
  }

  @GetMapping("/category/edit")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_UPDATE'})")
  public String editCategory(String id, Model model) {
    return "product/ViewCategory";
  }

  @PostMapping("/category/update")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_UPDATE'})")
  public String updateCategory(String id) {
    productCategoryService.deleteProductCategory(UUID.fromString(id));
    log.warn("Product category with id {} deleted successfully.", id);
    return "redirect:/product/categories";
  }

  @GetMapping("/category/view")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  public String ViewCategory(String id, Model model) {
    model.addAttribute(
        "category",
        categoryMapper.toResponse(productCategoryService.findById(UUID.fromString(id))));
    return "product/category/view";
  }
}
