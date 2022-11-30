package com.touchblankspot.inventory.types;

import lombok.NonNull;

public record GenericResponse(@NonNull String message, String error) {}
