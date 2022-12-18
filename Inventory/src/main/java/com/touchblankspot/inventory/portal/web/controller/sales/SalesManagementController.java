package com.touchblankspot.inventory.portal.web.controller.sales;

import com.touchblankspot.inventory.portal.data.enums.StockOperationEnum;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.service.SalesDetailsService;
import com.touchblankspot.inventory.portal.web.annotations.SalesController;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.stock.management.StockManagementRequestType;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SalesController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class SalesManagementController {

  @NonNull private final SalesDetailsService salesDetailsService;

  @NonNull private final ProductService productService;

  @GetMapping("/management")
  @PreAuthorize("@permissionService.hasPermission({'SALES_CREATE'})")
  public String createSales(Model model) {
    model.addAttribute("managementForm", new StockManagementRequestType());
    List<SelectType> productSelectList = productService.getProductSelectList();
    List<SelectType> stockOperations = StockOperationEnum.getSelectList();
    model.addAttribute("productSelectList", productSelectList);
    model.addAttribute("selectedProductId", productSelectList.get(0).id());
    return "sales/management/create";
  }

  /*@PostMapping("/management")
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
  }*/

}
