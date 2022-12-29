package com.touchblankspot.common.validator;

import java.util.List;

public interface IsUniqueInputFields {

  /**
   * @param fields Array of filed name
   * @param values Array of field values, it should be in same order as field name
   * @param isUpdate It represents whether annotation is used to update operation
   * @return It represents whether inputs are unique or it is matching with any existing record
   * @throws UnsupportedOperationException Throws Exception is selected field is not supported
   */
  boolean isUniqueRowCombination(List<String> fields, List<Object> values, Boolean isUpdate)
      throws UnsupportedOperationException;
}
