package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.data.model.Product;
import com.touchblankspot.inventory.portal.service.CategoryService;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.web.annotations.ProductController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.ProductMapper;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductManagementRequestType;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductManagementResponseType;
import jakarta.validation.Valid;
import java.util.Comparator;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@ProductController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductManagementController extends BaseController {

  @NonNull private final CategoryService productCategoryService;

  @NonNull private final ProductService productService;
  @NonNull private final ProductMapper productMapper;

  @GetMapping("/management")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CREATE'})")
  public String createProductCategory(Model model) {
    model.addAttribute("managementForm", new ProductManagementRequestType());
    model.addAttribute("categories", productCategoryService.getCategoryList());
    return "product/management/create";
  }

  @PostMapping("/management")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CREATE'})")
  public String createProductCategory(
      @Valid @ModelAttribute("managementForm") ProductManagementRequestType requestType,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      List<String> categories = productCategoryService.getCategoryList();
      model.addAttribute("categories", categories);
      model.addAttribute(
          "selectedCategoryId",
          requestType.getCategoryId() != null ? requestType.getCategoryId() : "");
      if (!ObjectUtils.isEmpty(requestType.getCategoryName())) {
        List<SelectType> subCategories =
            productCategoryService.getSubCategorySelectList(requestType.getCategoryName());
        model.addAttribute("subCategories", subCategories);
        model.addAttribute(
            "selectedCategoryId",
            requestType.getCategoryId() != null ? requestType.getCategoryId() : "");
      }
      if (requestType.getCategoryId() != null) {
        model.addAttribute("existingProducts", getExistingProductList(requestType.getCategoryId()));
      }
      return "product/management/create";
    }
    try {
      productService.save(productMapper.toEntity(requestType));
      model.addAttribute("successMessage", "Product Created successfully.");
    } catch (Exception ex) {
      log.error("Unable to create category", ex);
      model.addAttribute("errorMessage", "Unable to create Product. please contact administrator");
    }
    return "redirect:/product/management";
  }

  @GetMapping("/management/list")
  @PreAuthorize("@permissionService.hasPermission({'PROD_VIEW'})")
  public String getAll(
      Model model,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(pageSizeList.get(0));

    Page<Object[]> productPage =
        productService.getListData(PageRequest.of(currentPage - 1, pageSize));
    List<ProductManagementResponseType> responseTypeList =
        productPage.getContent().stream().map(ProductManagementResponseType::new).toList();
    int totalPages = productPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers =
          IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("ProductPage", productPage);
    model.addAttribute("Products", responseTypeList);
    model.addAttribute("currentPageNumber", currentPage);
    model.addAttribute("PageSizeList", pageSizeList);
    model.addAttribute("selectedPageSize", pageSizeList.get(0));
    return "product/management/show";
  }

  @GetMapping("/management/delete")
  @PreAuthorize("@permissionService.hasPermission({'PROD_DELETE'})")
  public String deleteCategory(String id) {
    productService.deleteProduct(UUID.fromString(id));
    log.warn("Product with id {} deleted successfully.", id);
    return "redirect:/product/management/list";
  }

  @GetMapping(
      value = "/existingProducts",
      produces = "application/json",
      consumes = "application/json")
  @ResponseBody
  @PreAuthorize("@permissionService.hasPermission({'PROD_VIEW'})")
  public ResponseEntity<List<String>> getProductSubCategories(@RequestParam UUID categoryId) {
    return ResponseEntity.ok(getExistingProductList(categoryId));
  }

  @GetMapping(value = "/select/types", produces = "application/json", consumes = "application/json")
  @ResponseBody
  @PreAuthorize("@permissionService.hasPermission({'PROD_VIEW'})")
  public ResponseEntity<List<SelectType>> getProductSelectList(@RequestParam UUID categoryId) {
    List<SelectType> productSelectTypes =
        productService.findByIdCategoryId(categoryId).stream()
            .map(product -> new SelectType(product.getId().toString(), product.getName()))
            .distinct()
            .sorted(Comparator.comparing(SelectType::value))
            .toList();
    return ResponseEntity.ok(productSelectTypes);
  }

  private List<String> getExistingProductList(UUID categoryId) {
    return productService.findByIdCategoryId(categoryId).stream()
        .map(Product::getName)
        .distinct()
        .sorted()
        .toList();
  }
}
