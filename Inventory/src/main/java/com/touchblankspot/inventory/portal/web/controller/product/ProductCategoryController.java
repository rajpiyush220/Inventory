package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.data.model.ProductCategory;
import com.touchblankspot.inventory.portal.service.ProductCategoryService;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.mapper.ProductCategoryMapper;
import com.touchblankspot.inventory.portal.web.types.product.category.ProductCategoryRequestType;
import com.touchblankspot.inventory.portal.web.types.product.category.ProductCategoryResponseType;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
// @HasProductAccess
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductCategoryController extends BaseController {

  @NonNull private final ProductCategoryService productCategoryService;

  @NonNull private final ProductCategoryMapper productCategoryMapper;

  @GetMapping("/product/categories")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  public String getAll(
      Model model,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(pageSizeList.get(0));

    Page<ProductCategory> productCategoryPage =
        productCategoryService.findAll(PageRequest.of(currentPage - 1, pageSize));
    List<ProductCategoryResponseType> responseTypeList =
        productCategoryPage.stream().map(productCategoryMapper::toResponse).toList();
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
    return "product/ShowCategories";
  }

  @GetMapping("/product/category")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_CREATE'})")
  public String createProductCategory(Model model) {
    model.addAttribute("categoryForm", new ProductCategoryRequestType());
    return "product/CreateCategory";
  }

  @PostMapping("/product/category")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_CREATE'})")
  public String createProductCategory(
      @Valid @ModelAttribute("categoryForm") ProductCategoryRequestType requestType,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return "product/CreateCategory";
    }
    try {
      productCategoryService.save(productCategoryMapper.toEntity(requestType));
      model.addAttribute("successMessage", "Product category Created successfully.");
    } catch (Exception ex) {
      log.error("Unable to create category", ex);
      model.addAttribute(
          "errorMessage", "Unable to create Product category. please contact administrator");
    }
    return "product/CreateCategory";
  }

  @GetMapping("/product/category/search")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  public @ResponseBody List<String> getSearchSuggestion(String searchKey, String type) {
    return switch (type.toLowerCase()) {
      case "category" -> productCategoryService.findByCategoryContains(searchKey);
      case "subcategory" -> productCategoryService.findBySubCategoryContains(searchKey);
      case "productsize" -> productCategoryService.findByProductSizeContains(searchKey);
      default -> new ArrayList<>();
    };
  }

  @GetMapping("/product/category/delete")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_DELETE'})")
  public String deleteCategory(String id) {
    productCategoryService.deleteProductCategory(UUID.fromString(id));
    log.warn("Product category with id {} deleted successfully.", id);
    return "redirect:/product/categories";
  }

  @GetMapping("/product/category/edit")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_UPDATE'})")
  public String editCategory(String id) {
    productCategoryService.deleteProductCategory(UUID.fromString(id));
    log.warn("Product category with id {} deleted successfully.", id);
    return "product/ViewCategory";
  }

  @PostMapping("/product/category/update")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_UPDATE'})")
  public String updateCategory(String id) {
    productCategoryService.deleteProductCategory(UUID.fromString(id));
    log.warn("Product category with id {} deleted successfully.", id);
    return "redirect:/product/categories";
  }

  @GetMapping("/product/category/view")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CAT_VIEW'})")
  public String ViewCategory(String id, Model model) {
    ProductCategory productCategory = productCategoryService.findById(UUID.fromString(id));
    model.addAttribute("category", productCategoryMapper.toResponse(productCategory));
    return "product/ViewCategory";
  }
}
