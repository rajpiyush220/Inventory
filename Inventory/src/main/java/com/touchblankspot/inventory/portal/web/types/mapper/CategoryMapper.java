package com.touchblankspot.inventory.portal.web.types.mapper;

import com.touchblankspot.inventory.portal.data.model.Category;
import com.touchblankspot.inventory.portal.web.types.product.category.CategoryRequestType;
import com.touchblankspot.inventory.portal.web.types.product.category.CategoryResponseType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

  Category toEntity(CategoryRequestType request);

  CategoryResponseType toResponse(Category productCategory);
}
