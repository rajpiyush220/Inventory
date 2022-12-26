package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.service.CategoryService;
import com.touchblankspot.inventory.portal.service.ProductPriceService;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.web.annotations.ProductController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.ProductPriceMapper;
import com.touchblankspot.inventory.portal.web.types.product.price.ProductPriceRequestType;
import com.touchblankspot.inventory.portal.web.types.product.price.ProductPriceResponseType;
import com.touchblankspot.inventory.portal.web.types.product.price.validator.ProductPriceValidator;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ProductController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PriceController extends BaseController {

  @NonNull private final ProductPriceService productPriceService;

  @NonNull private final ProductService productService;

  @NonNull private final CategoryService categoryService;

  @NonNull private final ProductPriceMapper productPriceMapper;

  @NonNull private final ProductPriceValidator productPriceValidator;

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
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(pageSizeList.get(0));

    Page<Object[]> productPricePage =
        productPriceService.getListData(PageRequest.of(currentPage - 1, pageSize));
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
    model.addAttribute("selectedPageSize", pageSizeList.get(0));
    return "product/price/show";
  }

  @GetMapping("/price/delete")
  @PreAuthorize("@permissionService.hasPermission({'PROD_PRICE_DELETE'})")
  public String deleteProductPrice(String id) {
    productPriceService.deleteProduct(UUID.fromString(id));
    log.warn("Product with id {} deleted successfully.", id);
    return "redirect:/product/prices";
  }
}
