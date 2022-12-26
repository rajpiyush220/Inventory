package com.touchblankspot.inventory.portal.web.types.mapper;

import com.touchblankspot.inventory.portal.data.model.StockAudit;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.web.types.stock.management.StockManagementRequestType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ProductService.class})
public interface StockAuditMapper {

  @Mapping(target = "product", source = "productId")
  StockAudit toEntity(StockManagementRequestType request);
}
