package com.touchblankspot.inventory.portal.web.types.mapper;

import com.touchblankspot.inventory.portal.data.model.Product;
import com.touchblankspot.inventory.portal.service.ProductCategoryService;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductManagementRequestType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ProductCategoryService.class})
public interface ProductMapper {

  @Mapping(target = "productCategory", source = "categoryId")
  Product toEntity(ProductManagementRequestType request);
}
