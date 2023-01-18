package com.touchblankspot.common.validator;

public interface FieldValueExists {

  boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException;
}
