package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.web.annotations.ProductController;
import com.touchblankspot.inventory.portal.web.types.product.category.ProductCategoryRequestType;
import jakarta.validation.Valid;
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

  @GetMapping("/management")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CREATE'})")
  public String createProductCategory(Model model) {
    model.addAttribute("categoryForm", new ProductCategoryRequestType());
    return "product/category/create";
  }

  @PostMapping("/management")
  @PreAuthorize("@permissionService.hasPermission({'PROD_CREATE'})")
  public String createProductCategory(
      @Valid @ModelAttribute("categoryForm") ProductCategoryRequestType requestType,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return "product/category/create";
    }
    try {
      //productCategoryService.save(productCategoryMapper.toEntity(requestType));
      model.addAttribute("successMessage", "Product category Created successfully.");
    } catch (Exception ex) {
      log.error("Unable to create category", ex);
      model.addAttribute(
          "errorMessage", "Unable to create Product category. please contact administrator");
    }
    return "product/category/create";
  }
}
