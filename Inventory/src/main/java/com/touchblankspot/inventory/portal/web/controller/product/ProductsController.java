package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.data.model.Product;
import com.touchblankspot.inventory.portal.service.CategoryService;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.web.annotations.ProductController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.AutoCompleteWrapper;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.ProductMapper;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductManagementRequestType;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductManagementResponseType;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductUpdateRequestType;
import com.touchblankspot.inventory.portal.web.types.product.price.validator.ProductPriceValidator;
import com.touchblankspot.inventory.portal.web.types.product.validator.ProductUpdateValidator;
import jakarta.validation.Valid;

import java.util.*;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@ProductController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductsController extends BaseController {

  @NonNull private final CategoryService productCategoryService;

  @NonNull private final ProductService productService;
  @NonNull private final ProductMapper productMapper;

  @NonNull private final ProductUpdateValidator productUpdateValidator;

  private static final List<SelectType> SEARCH_TYPES =
          List.of(
                  new SelectType("sname", "Short Name"),
                  new SelectType("name", "Name"),
                  new SelectType("mat", "Material"),
                  new SelectType("cat", "Category"),
                  new SelectType("subcat", "Sub Category"));

  private static final Map<String, String> SORT_COLUMN_MAP =
          Map.of(
                  "sname",
                  "shortName",
                  "name",
                  "name",
                  "mat",
                  "material",
                  "sdes",
                  "shortDescription",
                  "cat",
                  "categoryName",
                  "subcat",
                  "subCategory");

  private static final List<String> SEARCH_TYPE_KEYS =
          SEARCH_TYPES.stream().map(SelectType::id).toList();

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
          @RequestParam(value = "page", defaultValue = "1", required = false) Integer currentPage,
          @RequestParam(value = "size", defaultValue = "0", required = false) Integer pageSize,
          @RequestParam(value = "sortColumn", defaultValue = "sname", required = false)
          String sortColumn,
          @RequestParam(value = "sortOrder", defaultValue = "ASC", required = false) String sortOrder,
          @RequestParam(value = "searchKey", defaultValue = "", required = false) String searchKey,
          @RequestParam(value = "searchType", defaultValue = "", required = false) String searchType) {

    pageSize = pageSize > 0 ? pageSize : pageSizeList.get(0);
    sortOrder = sortOrder.toUpperCase();
    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), SORT_COLUMN_MAP.get(sortColumn));
    Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
    Page<Object[]> productPage = productService.getListData(pageable, searchType, searchKey);
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
    model.addAttribute("selectedPageSize", pageSize);
    model.addAttribute("sortOrder", sortOrder);
    model.addAttribute("sortColumn", sortColumn);
    model.addAttribute("currentPageSize", pageSize);
    model.addAttribute("searchType", searchType);
    model.addAttribute("searchKey", searchKey);
    model.addAttribute("searchTypes", SEARCH_TYPES);
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

  @GetMapping(value = "/suggestions/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("@permissionService.hasPermission({'PROD_VIEW'})")
  @ResponseBody
  public AutoCompleteWrapper getSearchSuggestion(String searchKey, String type) {
    return new AutoCompleteWrapper(
            SEARCH_TYPE_KEYS.stream().anyMatch(type::equalsIgnoreCase)
                    ? productService.getAutoCompleteSuggestions(type, searchKey)
                    : List.of());
  }

  @GetMapping("/management/view")
  @PreAuthorize("@permissionService.hasPermission({'PROD_VIEW'})")
  public String ViewProduct(String id, Model model) {
    model.addAttribute(
            "product", productMapper.toResponse(productService.findById(UUID.fromString(id))));
    return "product/management/view";
  }

  @GetMapping("/management/edit")
  @PreAuthorize("@permissionService.hasPermission({'PROD_UPDATE'})")
  public String editCategory(String id, Model model) {
    model.addAttribute(
            "productForm", productMapper.toUpdateRequest(productService.findById(UUID.fromString(id))));
    return "product/management/edit";
  }

  @PostMapping("/management/update")
  @PreAuthorize("@permissionService.hasPermission({'PROD_UPDATE'})")
  public String updateProduct(
          @Valid @ModelAttribute("productForm") ProductUpdateRequestType requestType,
          BindingResult bindingResult,
          Model model,
          Errors errors) {
    productUpdateValidator.validate(requestType,errors);
    model.addAttribute("productForm", requestType);
    if (!bindingResult.hasErrors() && !errors.hasErrors()) {
      try {
        productService.updateProduct(requestType);
        log.warn("Product with id {} updated successfully.", requestType.getId());
        model.addAttribute("successMessage", "Product updated Successfully");
      } catch (Exception ex) {
        log.error("Unable to update Product", ex);
        model.addAttribute("errorMessage", "Unable to update Product. Please contact Admin.");
      }
    }
    return "product/management/edit";
  }

  private List<String> getExistingProductList(UUID categoryId) {
    return productService.findByIdCategoryId(categoryId).stream()
        .map(Product::getName)
        .distinct()
        .sorted()
        .toList();
  }
}
