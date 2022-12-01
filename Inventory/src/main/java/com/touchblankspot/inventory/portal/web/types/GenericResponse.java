package com.touchblankspot.inventory.portal.web.types;

import lombok.NonNull;

public record GenericResponse(@NonNull String message, String error) {}
