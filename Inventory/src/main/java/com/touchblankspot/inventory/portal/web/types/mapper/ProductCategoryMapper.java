package com.touchblankspot.inventory.portal.web.types.mapper;

import com.touchblankspot.inventory.portal.data.model.ProductCategory;
import com.touchblankspot.inventory.portal.web.types.product.category.ProductCategoryRequestType;
import com.touchblankspot.inventory.portal.web.types.product.category.ProductCategoryResponseType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductCategoryMapper {

  ProductCategory toEntity(ProductCategoryRequestType request);

  ProductCategoryResponseType toResponse(ProductCategory productCategory);
}
