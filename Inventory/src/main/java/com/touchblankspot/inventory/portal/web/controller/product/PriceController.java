package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.service.CategoryService;
import com.touchblankspot.inventory.portal.service.ProductPriceService;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.web.annotations.ProductController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.AutoCompleteWrapper;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.ProductPriceMapper;
import com.touchblankspot.inventory.portal.web.types.product.price.ProductPriceRequestType;
import com.touchblankspot.inventory.portal.web.types.product.price.ProductPriceResponseType;
import com.touchblankspot.inventory.portal.web.types.product.price.validator.ProductPriceValidator;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@ProductController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PriceController extends BaseController {

  @NonNull private final ProductPriceService productPriceService;

  @NonNull private final ProductService productService;

  @NonNull private final CategoryService categoryService;

  @NonNull private final ProductPriceMapper productPriceMapper;

  @NonNull private final ProductPriceValidator productPriceValidator;

  private static final List<SelectType> SEARCH_TYPES =
          List.of(
                  new SelectType("sname", "Short Name"),
                  new SelectType("name", "Name"),
                  new SelectType("shortd", "Short Des"),
                  new SelectType("mat", "Material"),
                  new SelectType("psize", "Product Size"),
                  new SelectType("pprice", "Product Price"),
                  new SelectType("disper", "Dis Per"),
                  new SelectType("maxdisamt", "Max Disc Amt"));

  private static final Map<String, String> SORT_COLUMN_MAP =
          Map.of(
                  "sname",
                  "short_name",
                  "name",
                  "name",
                  "shortd",
                  "short_description",
                  "mat",
                  "material",
                  "psize",
                  "product_size",
                  "pprice",
                  "price",
                  "disper",
                  "discount_percentage",
                  "maxdisamt",
                  "max_discount_amount");

  private static final List<String> SEARCH_TYPE_KEYS =
          SEARCH_TYPES.stream().map(SelectType::id).toList();

  @GetMapping("/price")
  @PreAuthorize("@permissionService.hasPermission({'PROD_PRICE_CREATE'})")
  public String createProductCategory(Model model) {
    model.addAttribute("managementForm", new ProductPriceRequestType());
    model.addAttribute("categories", categoryService.getCategoryList());

    return "product/price/create";
  }

  @PostMapping("/price")
  @PreAuthorize("@permissionService.hasPermission({'PROD_PRICE_CREATE'})")
  public String createProductCategory(
      @Valid @ModelAttribute("managementForm") ProductPriceRequestType requestType,
      BindingResult bindingResult,
      Model model,
      Errors errors) {
    productPriceValidator.validate(requestType, errors);
    if (bindingResult.hasErrors() || errors.hasErrors()) {
      List<String> categories = categoryService.getCategoryList();
      model.addAttribute("categories", categories);
      model.addAttribute(
          "selectedCategory", requestType.getCategory() != null ? requestType.getCategory() : "");
      if (!ObjectUtils.isEmpty(requestType.getCategory())) {
        List<SelectType> subCategories =
            categoryService.getSubCategorySelectList(requestType.getCategory());
        model.addAttribute("subCategories", subCategories);
        model.addAttribute(
            "selectedSubCategoryId",
            requestType.getSubCategory() != null ? requestType.getSubCategory().toString() : "");
      }
      if (requestType.getSubCategory() != null) {
        model.addAttribute(
            "productSelectTypes",
            productService.findByIdCategoryId(requestType.getSubCategory()).stream()
                .map(product -> new SelectType(product.getId().toString(), product.getName()))
                .distinct()
                .sorted(Comparator.comparing(SelectType::value))
                .toList());
        if (requestType.getProductId() != null) {
          model.addAttribute("selectedProductId", requestType.getProductId());
        }
      }
      return "product/price/create";
    }
    try {
      productPriceService.save(productPriceMapper.toEntity(requestType));
      model.addAttribute("successMessage", "Product Price Created successfully.");
    } catch (Exception ex) {
      log.error("Unable to create Product Price", ex);
      model.addAttribute(
          "errorMessage", "Unable to create Product Price. please contact administrator");
    }
    return "redirect:/product/price";
  }

  @GetMapping("/prices")
  @PreAuthorize("@permissionService.hasPermission({'PROD_PRICE_VIEW'})")
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
    Page<Object[]> productPricePage = productPriceService.getListData(pageable, searchType, searchKey);
    List<ProductPriceResponseType> responseTypeList =
        productPricePage.getContent().stream().map(ProductPriceResponseType::new).toList();
    int totalPages = productPricePage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers =
          IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("ProductPricePage", productPricePage);
    model.addAttribute("ProductPrices", responseTypeList);
    model.addAttribute("currentPageNumber", currentPage);
    model.addAttribute("PageSizeList", pageSizeList);
    model.addAttribute("selectedPageSize", pageSize);
    model.addAttribute("sortOrder", sortOrder);
    model.addAttribute("sortColumn", sortColumn);
    model.addAttribute("currentPageSize", pageSize);
    model.addAttribute("searchType", searchType);
    model.addAttribute("searchKey", searchKey);
    model.addAttribute("searchTypes", SEARCH_TYPES);
    return "product/price/show";
  }

  @GetMapping("/price/delete")
  @PreAuthorize("@permissionService.hasPermission({'PROD_PRICE_DELETE'})")
  public String deleteProductPrice(String id) {
    productPriceService.deleteProduct(UUID.fromString(id));
    log.warn("Product with id {} deleted successfully.", id);
    return "redirect:/product/prices";
  }

  @GetMapping(value = "/price/suggestions/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("@permissionService.hasPermission({'PROD_PRICE_VIEW'})")
  @ResponseBody
  public AutoCompleteWrapper getSearchSuggestion(String searchKey, String type) {
    return new AutoCompleteWrapper(
            SEARCH_TYPE_KEYS.stream().anyMatch(type::equalsIgnoreCase)
                    ? productPriceService.getAutoCompleteSuggestions(type, searchKey)
                    : List.of());
  }
}
