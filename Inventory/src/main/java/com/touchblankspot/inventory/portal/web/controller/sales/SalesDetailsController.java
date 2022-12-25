package com.touchblankspot.inventory.portal.web.controller.sales;

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
import com.touchblankspot.inventory.portal.service.StockAuditService;
import com.touchblankspot.inventory.portal.service.StockService;
import com.touchblankspot.inventory.portal.web.annotations.SalesController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.SalesDetailMapper;
import com.touchblankspot.inventory.portal.web.types.sales.SalesDetailRequestType;
import com.touchblankspot.inventory.portal.web.types.sales.SalesDetailResponseType;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SalesController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class SalesDetailsController extends BaseController {

  @NonNull private final SalesDetailsService salesDetailsService;

  @NonNull private final SecurityService securityService;

  @NonNull private final ProductService productService;

  @NonNull private final ProductPriceService productPriceService;

  @NonNull private final StockService stockService;

  @NonNull private final StockAuditService stockAuditService;

  @NonNull private final SalesDetailMapper salesDetailMapper;

  @GetMapping("/detail/create")
  @PreAuthorize("@permissionService.hasPermission({'SALES_CREATE'})")
  public String createSales(Model model) {
    model.addAttribute("managementForm", new SalesDetailRequestType());
    List<SelectType> productSelectList = productService.getProductSelectList();
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
      model.addAttribute("productSelectTypes", productSelectList);
      model.addAttribute("paymentModes", PaymentModeEnum.getSelectList());
      model.addAttribute("selectedPaymentMode", PaymentModeEnum.CASH.name());
      model.addAttribute(
          "selectedProductId",
          requestType.getProductId() != null ? requestType.getProductId() : "");
      if (requestType.getProductId() != null) {
        model.addAttribute(
            "productSizes", productPriceService.getProductSize(requestType.getProductId()));
        model.addAttribute("selectedSize", requestType.getSize());
      }
      if (requestType.getProductId() != null && !Objects.equals(requestType.getSize(), "")) {
        BigDecimal price =
            productPriceService.getProductPrice(requestType.getProductId(), requestType.getSize());
        model.addAttribute("productPrice", price);
        if (requestType.getQuantity() != null && requestType.getQuantity().longValue() > 0) {
          model.addAttribute(
              "totalPrice",
              price
                  .multiply(BigDecimal.valueOf(requestType.getQuantity()))
                  .setScale(2, RoundingMode.DOWN));
        }
      }
      return "sales/details/create";
    }
    try {
      if (requestType.getCurrentStock() < requestType.getQuantity().longValue()) {
        model.addAttribute("errorMessage", "Quantity can not be greater than current stock.");
        return "sales/details/create";
      }
      ProductPrice productPrice =
          productPriceService
              .findByProductAndSize(requestType.getProductId(), requestType.getSize())
              .orElseThrow(() -> new RuntimeException("Product price not found"));
      BigDecimal discountAmount = BigDecimal.ZERO;
      if (productPrice.getDiscountPercentage() != null
          && !productPrice.getDiscountPercentage().equals("")
          && productPrice.getDiscountPercentage().length() > 0) {
        int discountPercentage = Integer.parseInt(productPrice.getDiscountPercentage());
        discountAmount =
            productPrice
                .getPrice()
                .multiply(BigDecimal.valueOf(discountPercentage))
                .divide(BigDecimal.valueOf(100L), RoundingMode.DOWN);

        if (productPrice.getMaxDiscountAmount() != null
            && productPrice.getMaxDiscountAmount() > 0) {
          discountAmount =
              discountAmount.longValue() > productPrice.getMaxDiscountAmount()
                  ? BigDecimal.valueOf(productPrice.getMaxDiscountAmount())
                  : discountAmount;
        }
        discountAmount =
            discountAmount.longValue() > productPrice.getMaxDiscountAmount()
                ? BigDecimal.valueOf(productPrice.getMaxDiscountAmount())
                : discountAmount;
      }

      BigDecimal totalPrice =
          productPrice
              .getPrice()
              .subtract(discountAmount)
              .multiply(BigDecimal.valueOf(requestType.getQuantity()));

      SalesDetails salesDetails = salesDetailMapper.toEntity(requestType);
      salesDetails.setSoldBy(securityService.getCurrentUserName());
      salesDetails.setTotalPrice(totalPrice.setScale(2, RoundingMode.DOWN));
      salesDetails.setDiscountAmount(
          discountAmount.multiply(
              BigDecimal.valueOf(requestType.getQuantity()).setScale(2, RoundingMode.DOWN)));
      salesDetails = salesDetailsService.save(salesDetails);

      // create stock audit and reduce quantity from stock
      Stock stock;
      if (requestType.getCurrentStock() > 0) {
        // update existing stock
        stock =
            stockService
                .findByProductIdAndProductSize(requestType.getProductId(), requestType.getSize())
                .orElse(null);
        if (stock != null) {
          stock.setQuantity((stock.getQuantity() - requestType.getQuantity().longValue()));
          stockService.save(stock);

          StockAudit stockAudit =
              StockAudit.builder()
                  .stock(stock)
                  .quantity(requestType.getQuantity().longValue())
                  .operationType(StockOperationEnum.SELL)
                  .operatedBy(securityService.getCurrentUserName())
                  .product(productService.findById(requestType.getProductId()))
                  .transactionTime(OffsetDateTime.now())
                  .build();
          stockAudit = stockAuditService.save(stockAudit);
          log.info("Stock updated with id {}", stock.getId());
          log.info("Stock audit created with id {}", stockAudit.getId());
        }
      }
      log.info("Sales details created with id {}", salesDetails.getId());
      model.addAttribute("successMessage", "Sales details added successfully.");
    } catch (Exception ex) {
      log.error("Unable to create sales details ", ex);
      model.addAttribute("errorMessage", "Unable to create Sales details. Please contact admin.");
    }
    return "sales/details/create";
  }

  @GetMapping("/details")
  @PreAuthorize("@permissionService.hasPermission({'SALES_VIEW'})")
  public String getAll(
      Model model,
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "0") Integer size) {
    int currentPage = page > 0 ? page : 1;
    int pageSize = size > 0 ? size : pageSizeList.get(0);

    Page<Object[]> productPage =
        salesDetailsService.getListData(PageRequest.of(currentPage - 1, pageSize));
    List<SalesDetailResponseType> responseTypeList =
        productPage.getContent().stream().map(SalesDetailResponseType::new).toList();
    int totalPages = productPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers =
          IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("SalePage", productPage);
    model.addAttribute("SaleDetails", responseTypeList);
    model.addAttribute("currentPageNumber", currentPage);
    model.addAttribute("PageSizeList", pageSizeList);
    model.addAttribute("selectedPageSize", pageSizeList.get(0));
    return "sales/details/show";
  }

  @GetMapping(value = "/detail/size", produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<List<String>> getProductSize(@RequestParam UUID productId) {
    return ResponseEntity.ok(productPriceService.getProductSize(productId));
  }

  @GetMapping(value = "/detail/price", produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<String> getProductPrice(
      @RequestParam UUID productId, @RequestParam String size) {
    return ResponseEntity.ok(productPriceService.getProductPrice(productId, size).toString());
  }
}