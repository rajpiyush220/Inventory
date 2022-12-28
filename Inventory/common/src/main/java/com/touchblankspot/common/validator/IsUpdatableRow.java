package com.touchblankspot.common.validator;

public interface IsUpdatableRow {

  boolean isUpdatableRow(
      String id,
      String firstField,
      Object firstFieldValue,
      String secondField,
      Object secondFieldValue)
      throws UnsupportedOperationException;
}
