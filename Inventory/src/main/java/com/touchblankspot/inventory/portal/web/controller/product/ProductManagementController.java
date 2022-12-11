package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.service.ProductCategoryService;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.web.annotations.ProductController;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.ProductMapper;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductManagementRequestType;
import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@ProductController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductManagementController {

  @NonNull private final ProductCategoryService productCategoryService;

  @NonNull private final ProductService productService;
  @NonNull private final ProductMapper productMapper;

  @GetMapping("/management")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CREATE'})")
  public String createProductCategory(Model model) {
    model.addAttribute("managementForm", new ProductManagementRequestType());
    List<SelectType> categories = productCategoryService.getCategorySelectList();
    model.addAttribute("categories", categories);
    model.addAttribute("selectedCategoryId", categories.get(0).id());
    return "product/management/create";
  }

  @PostMapping("/management")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CREATE'})")
  public String createProductCategory(
      @Valid @ModelAttribute("managementForm") ProductManagementRequestType requestType,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      List<SelectType> categories = productCategoryService.getCategorySelectList();
      model.addAttribute("categories", categories);
      model.addAttribute(
          "selectedCategoryId",
          requestType.getCategoryId() != null
              ? requestType.getCategoryId()
              : categories.get(0).id());
      return "product/management/create";
    }
    try {
      productService.save(productMapper.toEntity(requestType));
      model.addAttribute("successMessage", "Product Created successfully.");
    } catch (Exception ex) {
      log.error("Unable to create category", ex);
      model.addAttribute("errorMessage", "Unable to create Product. please contact administrator");
    }
    return "product/management/create";
  }
}
