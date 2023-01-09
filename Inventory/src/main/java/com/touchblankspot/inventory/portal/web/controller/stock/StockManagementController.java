package com.touchblankspot.inventory.portal.web.controller.stock;

import com.touchblankspot.inventory.portal.data.model.Stock;
import com.touchblankspot.inventory.portal.data.model.StockAudit;
import com.touchblankspot.inventory.portal.security.service.SecurityService;
import com.touchblankspot.inventory.portal.service.CategoryService;
import com.touchblankspot.inventory.portal.service.ProductPriceService;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.service.StockAuditService;
import com.touchblankspot.inventory.portal.service.StockService;
import com.touchblankspot.inventory.portal.web.annotations.StockController;
import com.touchblankspot.inventory.portal.web.controller.BaseController;
import com.touchblankspot.inventory.portal.web.types.AutoCompleteWrapper;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.mapper.StockAuditMapper;
import com.touchblankspot.inventory.portal.web.types.mapper.StockMapper;
import com.touchblankspot.inventory.portal.web.types.stock.management.StockAuditResponseType;
import com.touchblankspot.inventory.portal.web.types.stock.management.StockManagementRequestType;
import com.touchblankspot.inventory.portal.web.types.stock.management.StockManagementResponseType;
import jakarta.validation.Valid;

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

@StockController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class StockManagementController extends BaseController {

    @NonNull
    private final StockService stockService;

    @NonNull
    private final StockAuditService stockAuditService;

    @NonNull
    private final ProductService productService;

    @NonNull
    private final StockMapper stockMapper;

    @NonNull
    private final StockAuditMapper stockAuditMapper;

    @NonNull
    private final SecurityService securityService;

    @NonNull
    private final ProductPriceService productPriceService;

    @NonNull
    private final CategoryService categoryService;

    private static final List<SelectType> STOCK_SEARCH_TYPES =
            List.of(
                    new SelectType("name", "Name"),
                    new SelectType("sname", "Short Name"),
                    new SelectType("sdes", "Short Des"),
                    new SelectType("psize", "Product Size"),
                    new SelectType("cat", "Category"),
                    new SelectType("subcat", "Sub Category"),
                    new SelectType("quan", "Quantity"));

    private static final List<SelectType> STOCK_AUDIT_SEARCH_TYPES =
            List.of(
                    new SelectType("name", "Name"),
                    new SelectType("sname", "Short Name"),
                    new SelectType("sdes", "Short Des"),
                    new SelectType("otype", "Op Type"),
                    new SelectType("qty", "Quantity"),
                    new SelectType("opt", "Operator"));

    private static final Map<String, String> STOCK_SORT_COLUMN_MAP =
            Map.of(
                    "sname",
                    "shortName",
                    "name",
                    "name",
                    "sdes",
                    "shortDescription",
                    "psize",
                    "productSize",
                    "cat",
                    "categoryName",
                    "subcat",
                    "subCategory",
                    "quan",
                    "quantity");

    private static final Map<String, String> STOCK_AUDIT_SORT_COLUMN_MAP =
            Map.of(
                    "sname",
                    "shortName",
                    "name",
                    "name",
                    "sdes",
                    "shortDescription",
                    "otype",
                    "operation_type",
                    "qty",
                    "quantity",
                    "opt",
                    "operated_by");

    private static final List<String> STOCK_SEARCH_TYPE_KEYS =
            STOCK_SEARCH_TYPES.stream().map(SelectType::id).toList();

    private static final List<String> STOCK_AUDIT_SEARCH_TYPE_KEYS =
            STOCK_AUDIT_SEARCH_TYPES.stream().map(SelectType::id).toList();

    @GetMapping("/management")
    @PreAuthorize("@permissionService.hasPermission({'STOCK_CREATE'})")
    public String createStock(Model model) {
        model.addAttribute("managementForm", new StockManagementRequestType());
        model.addAttribute("categories", categoryService.getCategoryList());
        return "stock/management/create";
    }

    @PostMapping("/management")
    @PreAuthorize("@permissionService.hasPermission({'STOCK_CREATE'})")
    public String createStock(
            @Valid @ModelAttribute("managementForm") StockManagementRequestType requestType,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getCategoryList());
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
                        "productSelectList",
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
                model.addAttribute("selectedSize", requestType.getProductSize());
            }
            return "stock/management/create";
        }
        try {
            Stock stock = null;
            if (requestType.getCurrentStock() > 0) {
                // update existing stock
                stock =
                        stockService
                                .findByProductIdAndProductSize(
                                        requestType.getProductId(), requestType.getProductSize())
                                .orElse(null);
                if (stock != null) {
                    stock.setQuantity(stock.getQuantity() + requestType.getQuantity());
                }
            }
            stock = stockService.save(stock == null ? stockMapper.toEntity(requestType) : stock);
            StockAudit stockAudit = stockAuditMapper.toEntity(requestType);
            stockAudit.setOperatedBy(securityService.getCurrentUserName());
            stockAudit.setStock(stock);
            stockAudit.setTransactionTime(OffsetDateTime.now());
            stockAudit = stockAuditService.save(stockAudit);
            log.info("Stock created/updated with id {}", stock.getId());
            log.info("Stock audit created with id {}", stockAudit.getId());
            model.addAttribute("successMessage", "Stock Created/Updated successfully.");
        } catch (Exception ex) {
            log.error("Unable to create stock", ex);
            model.addAttribute("errorMessage", "Unable to create Stock. please contact administrator");
        }
        return "redirect:/stock/management";
    }

    @GetMapping("/management/list")
    @PreAuthorize("@permissionService.hasPermission({'STOCK_VIEW'})")
    public String getAll(
            Model model,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer currentPage,
            @RequestParam(value = "size", defaultValue = "0", required = false) Integer pageSize,
            @RequestParam(value = "sortColumn", defaultValue = "sname", required = false)
            String sortColumn,
            @RequestParam(value = "sortOrder", defaultValue = "ASC", required = false) String sortOrder,
            @RequestParam(value = "searchKey", defaultValue = "", required = false) String searchKey,
            @RequestParam(value = "searchType", defaultValue = "", required = false) String searchType) {
        pageSize = pageSize > 0 ? pageSize : pageSizeList.get(0);
        sortOrder = sortOrder.toUpperCase();
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), STOCK_SORT_COLUMN_MAP.get(sortColumn));
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        Page<Object[]> productPage = stockService.getListData(pageable, searchType, searchKey);
        List<StockManagementResponseType> responseTypeList =
                productPage.getContent().stream().map(StockManagementResponseType::new).toList();
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers =
                    IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("StockPage", productPage);
        setModelAttributes(model,
                Map.of("PageSizeList", pageSizeList,
                        "currentPageNumber", currentPage,
                        "Stocks", responseTypeList,
                        "selectedPageSize", pageSize,
                        "sortOrder", sortOrder,
                        "sortColumn", sortColumn,
                        "currentPageSize", pageSize,
                        "searchType", searchType,
                        "searchKey", searchKey,
                        "searchTypes", STOCK_SEARCH_TYPES));
        return "stock/management/show";
    }

    @GetMapping(value = "/management/stock_search", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@permissionService.hasPermission({'STOCK_VIEW'})")
    @ResponseBody
    public AutoCompleteWrapper getStockSearchSuggestion(String searchKey, String type) {
        return new AutoCompleteWrapper(
                STOCK_SEARCH_TYPE_KEYS.stream().anyMatch(type::equalsIgnoreCase)
                        ? stockService.getAutoCompleteSuggestions(type, searchKey) :
                        List.of());
    }

    @GetMapping(value = "/management/stock_audit_search", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@permissionService.hasPermission({'STOCK_AUDIT_VIEW'})")
    @ResponseBody
    public AutoCompleteWrapper getStockAuditSearchSuggestion(String searchKey, String type) {
        return new AutoCompleteWrapper(
                STOCK_AUDIT_SEARCH_TYPE_KEYS.stream().anyMatch(type::equalsIgnoreCase)
                        ? stockAuditService.getAutoCompleteSuggestions(type, searchKey) :
                        List.of());
    }

    @GetMapping("/audit/list")
    @PreAuthorize("@permissionService.hasPermission({'STOCK_AUDIT_VIEW'})")
    public String getStockAudits(
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
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), STOCK_AUDIT_SORT_COLUMN_MAP.get(sortColumn));
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        Page<Object[]> productPage =
                stockAuditService.getListData(pageable, searchType, searchKey);
        List<StockAuditResponseType> responseTypeList =
                productPage.getContent().stream().map(StockAuditResponseType::new).toList();
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers =
                    IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("Stocks", responseTypeList);
        setModelAttributes(model,
                Map.of("StockPage", productPage,
                        "currentPageNumber", currentPage,
                        "PageSizeList", pageSizeList,
                        "selectedPageSize", pageSize,
                        "sortOrder", sortOrder,
                        "sortColumn", sortColumn,
                        "currentPageSize", pageSize,
                        "searchType", searchType,
                        "searchKey", searchKey,
                        "searchTypes", STOCK_AUDIT_SEARCH_TYPES));
        return "stock/management/audit_show";
    }

    @GetMapping(value = "/status", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> getProductStock(
            @RequestParam UUID productId, @RequestParam String size) {
        return ResponseEntity.ok(stockService.getCurrentStock(productId, size).toString());
    }
}
