package com.touchblankspot.inventory.portal.web.types;

import java.util.List;
import lombok.NonNull;

public record AutoCompleteWrapper(@NonNull List<String> suggestions) {}
