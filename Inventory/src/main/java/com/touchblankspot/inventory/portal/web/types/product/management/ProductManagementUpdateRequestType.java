package com.touchblankspot.inventory.portal.web.types.product.management;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductManagementUpdateRequestType {
    private UUID id;

    @Size(min = 2, max = 50, message = "Product short name must be between 2 and 50 character.")
    private String shortName;

    @Size(min = 2, max = 50, message = "Product name must be between 2 and 50 character.")
    private String name;

    @Size(min = 2, max = 50, message = "Product shortDescription must be between 2 and 50 character.")
    private String shortDescription;

    @Size(min = 2, max = 255, message = "Product description must be between 2 and 255 character.")
    private String description;

    @Size(min = 2, max = 50, message = "Product material must be between 2 and 50 character.")
    private String material;
}
