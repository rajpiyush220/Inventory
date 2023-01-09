package com.touchblankspot.inventory.portal.web.types.mapper;

import com.touchblankspot.inventory.portal.data.model.Product;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductManagementRequestType;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductManagementResponseType;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductUpdateRequestType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

  Product toEntity(ProductManagementRequestType request);

  @Mapping(target = "categoryName", source = "category.category")
  @Mapping(target = "subCategory", source = "category.subCategory")
  ProductManagementResponseType toResponse(Product product);

  ProductUpdateRequestType toUpdateRequest(Product product);
}
