package com.touchblankspot.inventory.portal.web.controller.sales;

import com.touchblankspot.inventory.portal.data.enums.PaymentModeEnum;
import com.touchblankspot.inventory.portal.data.enums.StockOperationEnum;
import com.touchblankspot.inventory.portal.data.model.ProductPrice;
import com.touchblankspot.inventory.portal.data.model.SalesDetails;
import com.touchblankspot.inventory.portal.data.model.Stock;
import com.touchblankspot.inventory.portal.data.model.StockAudit;
import com.touchblankspot.inventory.portal.security.service.SecurityService;
import com.touchblankspot.inventory.portal.service.CategoryService;
import com.touchblankspot.inventory.portal.service.ProductPriceService;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.service.SalesDetailsService;
import com.touchblankspot.inventory.portal.service.StockAuditService;
import com.touchblankspot.inventory.portal.service.StockService;
import com.touchblankspot.inventory.portal.web.annotations.SalesController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.AutoCompleteWrapper;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.SalesDetailMapper;
import com.touchblankspot.inventory.portal.web.types.sales.SalesDetailRequestType;
import com.touchblankspot.inventory.portal.web.types.sales.SalesDetailResponseType;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
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

  @NonNull private final CategoryService categoryService;

  @NonNull private final SalesDetailMapper salesDetailMapper;

  private static final List<SelectType> SALES_SEARCH_TYPES =
      List.of(
          new SelectType("name", "Name"),
          new SelectType("cat", "Category"),
          new SelectType("qty", "Quantity"),
          new SelectType("uprice", "Unit Price"),
          new SelectType("totprice", "Total Price"),
          new SelectType("payMode", "Pay Mode"),
          new SelectType("tranid", "Transaction Id"),
          new SelectType("opt", "Operator"));

  private static final Map<String, String> SALES_SORT_COLUMN_MAP =
      Map.of(
          "name", "name",
          "cat", "category",
          "totprice", "total_price",
          "qty", "quantity",
          "uprice", "unit_price",
          "payMode", "payment_mode",
          "tranid", "transaction_id",
          "opt", "sold_by");
  private static final List<String> SALES_SEARCH_TYPE_KEYS =
      SALES_SEARCH_TYPES.stream().map(SelectType::id).toList();

  @GetMapping("/detail/create")
  @PreAuthorize("@permissionService.hasPermission({'SALES_CREATE'})")
  public String createSales(Model model) {
    model.addAttribute("managementForm", new SalesDetailRequestType());
    model.addAttribute("categories", categoryService.getCategoryList());
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
      if (requestType.getProductId() != null) {
        model.addAttribute(
            "productSizes", productPriceService.getProductSize(requestType.getProductId()));
        model.addAttribute("selectedSize", requestType.getSize());
      }
      model.addAttribute("paymentModes", PaymentModeEnum.getSelectList());
      model.addAttribute(
          "selectedPaymentMode",
          requestType.getPaymentMode() != null
              ? requestType.getPaymentMode()
              : PaymentModeEnum.CASH.name());
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
    return "redirect:/sales/detail/create";
  }

  @GetMapping("/details")
  @PreAuthorize("@permissionService.hasPermission({'SALES_VIEW'})")
  public String getAll(
      Model model,
      @RequestParam(value = "page", defaultValue = "1", required = false) Integer currentPage,
      @RequestParam(value = "size", defaultValue = "0", required = false) Integer pageSize,
      @RequestParam(value = "sortColumn", defaultValue = "name", required = false)
          String sortColumn,
      @RequestParam(value = "sortOrder", defaultValue = "ASC", required = false) String sortOrder,
      @RequestParam(value = "searchKey", defaultValue = "", required = false) String searchKey,
      @RequestParam(value = "searchType", defaultValue = "", required = false) String searchType) {
    pageSize = getPageSize(pageSize);
    sortOrder = sortOrder.toUpperCase();
    Sort sort =
        Sort.by(Sort.Direction.fromString(sortOrder), SALES_SORT_COLUMN_MAP.get(sortColumn));
    Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

    Page<Object[]> salesDetailPage =
        salesDetailsService.getListData(pageable, searchType, searchKey);
    List<SalesDetailResponseType> responseTypeList =
        salesDetailPage.getContent().stream().map(SalesDetailResponseType::new).toList();
    int totalPages = salesDetailPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers =
          IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("SalePage", salesDetailPage);
    model.addAttribute("SaleDetails", responseTypeList);
    setModelAttributes(
        model,
        Map.of(
            "PageSizeList",
            pageSizeList,
            "currentPageNumber",
            currentPage,
            "Stocks",
            responseTypeList,
            "selectedPageSize",
            pageSize,
            "sortOrder",
            sortOrder,
            "sortColumn",
            sortColumn,
            "currentPageSize",
            pageSize,
            "searchType",
            searchType,
            "searchKey",
            searchKey,
            "searchTypes",
            SALES_SEARCH_TYPES));
    return "sales/details/show";
  }

  @GetMapping(value = "/detail/size", produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<List<String>> getProductSize(@RequestParam UUID productId) {
    return ResponseEntity.ok(productPriceService.getProductSize(productId));
  }

  @GetMapping(value = "/suggestions/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("@permissionService.hasPermission({'SALES_VIEW'})")
  @ResponseBody
  public AutoCompleteWrapper getStockSearchSuggestion(String searchKey, String type) {
    return new AutoCompleteWrapper(
        SALES_SEARCH_TYPE_KEYS.stream().anyMatch(type::equalsIgnoreCase)
            ? salesDetailsService.getAutoCompleteSuggestions(type, searchKey)
            : List.of());
  }

  @GetMapping(value = "/detail/price", produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<String> getProductPrice(
      @RequestParam UUID productId, @RequestParam String size) {
    return ResponseEntity.ok(productPriceService.getProductPrice(productId, size).toString());
  }
}
