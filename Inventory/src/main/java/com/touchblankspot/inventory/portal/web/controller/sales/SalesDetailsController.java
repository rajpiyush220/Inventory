package com.touchblankspot.inventory.portal.web.controller.sales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.touchblankspot.inventory.portal.data.enums.PaymentModeEnum;
import com.touchblankspot.inventory.portal.data.enums.StockOperationEnum;
import com.touchblankspot.inventory.portal.data.model.ProductPrice;
import com.touchblankspot.inventory.portal.data.model.SalesDetails;
import com.touchblankspot.inventory.portal.data.model.Stock;
import com.touchblankspot.inventory.portal.data.model.StockAudit;
import com.touchblankspot.inventory.portal.security.service.SecurityService;
import com.touchblankspot.inventory.portal.service.ProductPriceService;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.service.SalesDetailsService;
import com.touchblankspot.inventory.portal.web.annotations.SalesController;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.SalesDetailMapper;
import com.touchblankspot.inventory.portal.web.types.sales.SalesDetailRequestType;
import com.touchblankspot.inventory.portal.web.types.stock.management.StockManagementRequestType;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SalesController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class SalesDetailsController {

  @NonNull
  private final SalesDetailsService salesDetailsService;

  @NonNull
  private final SecurityService securityService;

  @NonNull
  private final ProductService productService;

  @NonNull
  private final ProductPriceService productPriceService;

  @NonNull
  private final SalesDetailMapper salesDetailMapper;

  @GetMapping("/detail/create")
  @PreAuthorize("@permissionService.hasPermission({'SALES_CREATE'})")
  public String createSales(Model model) {
    model.addAttribute("managementForm", new SalesDetailRequestType());
    List<SelectType> productSelectList = productService.getProductSelectList();
    List<SelectType> stockOperations = StockOperationEnum.getSelectList();
    model.addAttribute("productSelectTypes", productSelectList);
    model.addAttribute("paymentModes", PaymentModeEnum.getSelectList());
    model.addAttribute("selectedPaymentMode", PaymentModeEnum.CASH.name());
    return "sales/details/create";
  }

  @PostMapping("/detail/create")
  @PreAuthorize("@permissionService.hasPermission({'SALES_CREATE'})")
  public String createStock(
      @Valid @ModelAttribute("managementForm") SalesDetailRequestType requestType,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      List<SelectType> productSelectList = productService.getProductSelectList();
      List<SelectType> stockOperations = StockOperationEnum.getSelectList();
      model.addAttribute("productSelectTypes", productSelectList);
      model.addAttribute("paymentModes", PaymentModeEnum.getSelectList());
      model.addAttribute("selectedPaymentMode", PaymentModeEnum.CASH.name());
      model.addAttribute("selectedProductId",
          requestType.getProductId() != null ? requestType.getProductId() : "");
      if (requestType.getProductId() != null) {
        model.addAttribute("productSizes",
            productPriceService.getProductSize(requestType.getProductId()));
        model.addAttribute("selectedSize", requestType.getSize());
      }
      if (requestType.getProductId() != null && requestType.getSize() != "") {
        BigDecimal price = productPriceService.getProductPrice(requestType.getProductId(),
            requestType.getSize());
        model.addAttribute("productPrice",
            price);
        if (requestType.getQuantity() != null && requestType.getQuantity().longValue() > 0) {
          model.addAttribute("totalPrice", price.multiply(new BigDecimal(requestType.getQuantity()))
              .setScale(2, RoundingMode.DOWN));
        }
      }
      return "sales/details/create";
    }
    try {
      ProductPrice productPrice =
          productPriceService.findByProductAndSize(requestType.getProductId(),
                  requestType.getSize())
              .get();
      BigDecimal discountAmount = BigDecimal.ZERO;
      if (productPrice.getDiscountPercentage() != null &&
          productPrice.getDiscountPercentage() != "" &&
          productPrice.getDiscountPercentage().length() > 0) {
        Integer discountPercentage = Integer.valueOf(productPrice.getDiscountPercentage());
        discountAmount =
            productPrice.getPrice().multiply(BigDecimal.valueOf(discountPercentage.longValue()))
                .divide(BigDecimal.valueOf(100L));

        if (productPrice.getMaxDiscountAmount() != null &&
            productPrice.getMaxDiscountAmount().longValue() > 0) {
          discountAmount = discountAmount.longValue() > productPrice.getMaxDiscountAmount() ?
              BigDecimal.valueOf(productPrice.getMaxDiscountAmount()) :
              discountAmount;
        }
        discountAmount = discountAmount.longValue() > productPrice.getMaxDiscountAmount() ?
            BigDecimal.valueOf(productPrice.getMaxDiscountAmount()) :
            discountAmount;
      }

      BigDecimal totalPrice =
          productPrice.getPrice().subtract(discountAmount)
              .multiply(new BigDecimal(requestType.getQuantity()));


      SalesDetails salesDetails = salesDetailMapper.toEntity(requestType);
      salesDetails.setSoldBy(securityService.getCurrentUserName());
      salesDetails.setTotalPrice(totalPrice.setScale(2, RoundingMode.DOWN));
      salesDetails.setDiscountAmount(discountAmount.multiply(
          BigDecimal.valueOf(requestType.getQuantity()).setScale(2, RoundingMode.DOWN)));

      salesDetails = salesDetailsService.save(salesDetails);
      log.info("Sales details created with id {}", salesDetails.getId());
      model.addAttribute("successMessage", "Sales details added successfully.");
    } catch (Exception ex) {
      log.error("Unable to create sales details ", ex);
      model.addAttribute("errorMessage", "Unable to create Sales details. Please contact admin.");
    }
    return "sales/details/create";
  }

  @GetMapping(value = "/detail/size", produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<List<String>> getProductSize(@RequestParam UUID productId) {
    return ResponseEntity.ok(productPriceService.getProductSize(productId));
  }

  @GetMapping(value = "/detail/price", produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<String> getProductPrice(@RequestParam UUID productId,
                                                @RequestParam String size) {
    return ResponseEntity.ok(productPriceService.getProductPrice(productId, size).toString());
  }
}
