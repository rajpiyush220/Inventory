package com.touchblankspot.inventory.portal.web.types.mapper;

import com.touchblankspot.inventory.portal.data.model.SalesDetails;
import com.touchblankspot.inventory.portal.web.types.sales.SalesDetailRequestType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SalesDetailMapper {

  SalesDetails toEntity(SalesDetailRequestType request);
}
