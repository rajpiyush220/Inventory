package com.touchblankspot.common.validator;

public interface IsUniqueRowExists {

  boolean isUniqueRowCombination(
      String firstField, Object firstFieldValue, String secondField, Object secondFieldValue)
      throws UnsupportedOperationException;
}
