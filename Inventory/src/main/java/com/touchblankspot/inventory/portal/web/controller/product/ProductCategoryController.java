package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.security.annotations.HasProductAccess;
import com.touchblankspot.inventory.portal.service.ProductCategoryService;
import com.touchblankspot.inventory.portal.user.constant.RoleEnum;
import com.touchblankspot.inventory.portal.web.types.mapper.ProductCategoryMapper;
import com.touchblankspot.inventory.portal.web.types.product.category.ProductCategoryRequestType;
import com.touchblankspot.inventory.portal.web.types.product.category.ProductCategoryResponseType;
import com.touchblankspot.inventory.portal.web.types.user.RegisterUserRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
//@HasProductAccess
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductCategoryController {

  @NonNull
  private final ProductCategoryService productCategoryService;

  @NonNull
  private final ProductCategoryMapper productCategoryMapper;

  @GetMapping("/product/categories")
  public String getAll(Model model) {
    List<ProductCategoryResponseType> responseTypeList = productCategoryService.findAll().stream()
        .map(productCategory -> productCategoryMapper.toResponse(productCategory)).toList();
    model.addAttribute("ProductCategories", responseTypeList);
    return "product/ShowCategories";
  }

  @GetMapping("/product/category")
  //@HasProductAccess
  public String createProductCategory(Model model) {
    model.addAttribute("categoryForm", new ProductCategoryRequestType());
    return "product/CreateCategory";
  }

  @PostMapping("/product/category")
  //@HasProductAccess
  public String createProductCategory(
      @Valid @ModelAttribute("categoryForm") ProductCategoryRequestType requestType,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return "product/CreateCategory";
    }
    try {
      productCategoryService.save(productCategoryMapper.toEntity(requestType));
      model.addAttribute("successMessage", "Product category Created successfully.");
    } catch (Exception ex) {
      log.error("Unable to create category", ex);
      model.addAttribute("errorMessage",
          "Unable to create Product category. please contact administrator");
    }
    return "product/CreateCategory";
  }
}
