package com.touchblankspot.inventory.validator;

import com.touchblankspot.inventory.constant.InventoryConstant;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailValidator {

  private static final Pattern EMAIL_PATTERN = Pattern.compile(InventoryConstant.EMAIL_REGEX);

  public static Boolean isValidEmail(String emailAddress) {
    return EMAIL_PATTERN.matcher(emailAddress).matches();
  }
}
