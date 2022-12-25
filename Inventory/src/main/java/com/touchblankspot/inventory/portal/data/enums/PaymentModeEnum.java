package com.touchblankspot.inventory.portal.data.enums;

import com.touchblankspot.inventory.portal.web.types.SelectType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentModeEnum {
  CASH("Cash"),
  UPI("Upi"),
  CARD("Card");

  private final String description;

  private static final List<SelectType> selectTypes =
      Arrays.stream(PaymentModeEnum.values())
          .map(opEnum -> new SelectType(opEnum.name(), opEnum.description))
          .toList();

  public static List<SelectType> getSelectList() {
    return selectTypes.stream().toList();
  }
}
