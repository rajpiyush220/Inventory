package com.touchblankspot.common.validator;

public interface FieldValueExists {

  /**
   * Checks whether or not a given value exists for a given field
   *
   * @param value The value to check for
   * @param fieldName The name of the field for which to check if the value exists
   * @return True if the value exists for the field; false otherwise
   * @throws UnsupportedOperationException Throws exception id operation not supported
   */
  boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException;
}
