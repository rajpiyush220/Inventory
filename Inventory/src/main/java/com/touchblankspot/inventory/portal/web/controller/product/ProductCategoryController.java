package com.touchblankspot.inventory.portal.web.controller.product;

import com.touchblankspot.inventory.portal.data.model.ProductCategory;
import com.touchblankspot.inventory.portal.security.annotations.HasProductAccess;
import com.touchblankspot.inventory.portal.service.ProductCategoryService;
import com.touchblankspot.inventory.portal.user.constant.RoleEnum;
import com.touchblankspot.inventory.portal.web.types.mapper.ProductCategoryMapper;
import com.touchblankspot.inventory.portal.web.types.product.category.ProductCategoryRequestType;
import com.touchblankspot.inventory.portal.web.types.product.category.ProductCategoryResponseType;
import com.touchblankspot.inventory.portal.web.types.user.RegisterUserRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  public String getAll(Model model,
                       @RequestParam("page") Optional<Integer> page,
                       @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(5);

    Page<ProductCategory> productCategoryPage =
        productCategoryService.findAll(PageRequest.of(currentPage - 1, pageSize));
    List<ProductCategoryResponseType> responseTypeList = productCategoryPage.stream()
        .map(productCategory -> productCategoryMapper.toResponse(productCategory)).toList();
    int totalPages = productCategoryPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
          .boxed()
          .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("ProductCategoriesPage", productCategoryPage);
    model.addAttribute("ProductCategories", responseTypeList);
    model.addAttribute("currentPageNumber",currentPage);
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
