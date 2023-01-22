package com.touchblankspot.common.validator;

import java.util.List;

public interface IsUniqueInputFields {

  boolean isUniqueRowCombination(List<String> fields, List<Object> values, Boolean isUpdate)
      throws UnsupportedOperationException;
}
