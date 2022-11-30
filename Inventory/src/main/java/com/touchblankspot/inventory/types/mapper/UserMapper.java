package com.touchblankspot.inventory.types.mapper;

import com.touchblankspot.inventory.data.model.User;
import com.touchblankspot.inventory.types.user.UserRegistrationRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  User toEntity(UserRegistrationRequest request);
}
