package com.touchblankspot.inventory.portal.web.controller.stock;

import com.touchblankspot.inventory.portal.data.enums.StockOperationEnum;
import com.touchblankspot.inventory.portal.data.model.Stock;
import com.touchblankspot.inventory.portal.data.model.StockAudit;
import com.touchblankspot.inventory.portal.security.service.SecurityService;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.service.StockAuditService;
import com.touchblankspot.inventory.portal.service.StockService;
import com.touchblankspot.inventory.portal.web.annotations.StockController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.StockAuditMapper;
import com.touchblankspot.inventory.portal.web.types.mapper.StockMapper;
import com.touchblankspot.inventory.portal.web.types.stock.management.StockAuditResponseType;
import com.touchblankspot.inventory.portal.web.types.stock.management.StockManagementRequestType;
import com.touchblankspot.inventory.portal.web.types.stock.management.StockManagementResponseType;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@StockController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class StockManagementController extends BaseController {

  @NonNull private final StockService stockService;

  @NonNull private final StockAuditService stockAuditService;

  @NonNull private final ProductService productService;

  @NonNull private final StockMapper stockMapper;

  @NonNull private final StockAuditMapper stockAuditMapper;

  @NonNull private final SecurityService securityService;

  @GetMapping("/management")
  @PreAuthorize("@permissionService.hasPermission({'STOCK_CREATE'})")
  public String createStock(Model model) {
    model.addAttribute("managementForm", new StockManagementRequestType());
    List<SelectType> productSelectList = productService.getProductSelectList();
    List<SelectType> stockOperations = StockOperationEnum.getSelectList();
    model.addAttribute("productSelectList", productSelectList);
    model.addAttribute("selectedProductId", productSelectList.get(0).id());
    model.addAttribute("stockOperations", stockOperations);
    model.addAttribute("selectedStockOperations", stockOperations.get(0).id());
    return "stock/management/create";
  }

  @PostMapping("/management")
  @PreAuthorize("@permissionService.hasPermission({'STOCK_CREATE'})")
  public String createStock(
      @Valid @ModelAttribute("managementForm") StockManagementRequestType requestType,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      List<SelectType> productSelectList = productService.getProductSelectList();
      List<SelectType> stockOperations = StockOperationEnum.getSelectList();
      model.addAttribute("productSelectList", productSelectList);
      model.addAttribute(
          "selectedProductId",
          requestType.getProductId() != null
              ? requestType.getProductId()
              : productSelectList.get(0).id());
      model.addAttribute("stockOperations", stockOperations);
      model.addAttribute(
          "selectedStockOperations",
          requestType.getOperationType() != null
              ? requestType.getOperationType()
              : stockOperations.get(0).id());
      return "stock/management/create";
    }
    try {
      Stock stock = stockService.save(stockMapper.toEntity(requestType));
      StockAudit stockAudit = stockAuditMapper.toEntity(requestType);
      stockAudit.setOperatedBy(securityService.getCurrentUserName());
      stockAudit.setStock(stock);
      stockAudit = stockAuditService.save(stockAudit);
      log.info("Stock created with id {}", stock.getId());
      log.info("Stock audit created with id {}", stockAudit.getId());
      model.addAttribute("successMessage", "Stock Created/Updated successfully.");

    } catch (Exception ex) {
      log.error("Unable to create stock", ex);
      model.addAttribute("errorMessage", "Unable to create Stock. please contact administrator");
    }
    model.addAttribute("managementForm", new StockManagementRequestType());
    return "stock/management/create";
  }

  @GetMapping("/management/list")
  @PreAuthorize("@permissionService.hasPermission({'STOCK_VIEW'})")
  public String getAll(
      Model model,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(pageSizeList.get(0));

    Page<Object[]> productPage =
        stockService.getListData(PageRequest.of(currentPage - 1, pageSize));
    List<StockManagementResponseType> responseTypeList =
        productPage.getContent().stream().map(StockManagementResponseType::new).toList();
    int totalPages = productPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers =
          IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("StockPage", productPage);
    model.addAttribute("Stocks", responseTypeList);
    model.addAttribute("currentPageNumber", currentPage);
    model.addAttribute("PageSizeList", pageSizeList);
    model.addAttribute("selectedPageSize", pageSizeList.get(0));
    return "stock/management/show";
  }

  @GetMapping("/audit/list")
  @PreAuthorize("@permissionService.hasPermission({'STOCK_AUDIT_VIEW'})")
  public String getStockAudits(
      Model model,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(pageSizeList.get(0));

    Page<Object[]> productPage =
        stockAuditService.getListData(PageRequest.of(currentPage - 1, pageSize));
    List<StockAuditResponseType> responseTypeList =
        productPage.getContent().stream().map(StockAuditResponseType::new).toList();
    int totalPages = productPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers =
          IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    model.addAttribute("StockPage", productPage);
    model.addAttribute("Stocks", responseTypeList);
    model.addAttribute("currentPageNumber", currentPage);
    model.addAttribute("PageSizeList", pageSizeList);
    model.addAttribute("selectedPageSize", pageSizeList.get(0));
    return "stock/management/audit_show";
  }
}
