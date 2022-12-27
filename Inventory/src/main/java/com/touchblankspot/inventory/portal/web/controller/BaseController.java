package com.touchblankspot.inventory.portal.web.controller;

import com.touchblankspot.inventory.portal.web.types.SelectType;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseController {

  private static List<SelectType> pageSizeSelectTypes;

  @Value("${application.pagination.pageSizeList}")
  public List<Integer> pageSizeList;

  protected List<SelectType> getPageSizeSelectTypes() {
    if (pageSizeSelectTypes == null) {
      pageSizeSelectTypes =
          pageSizeList.stream()
              .map(size -> new SelectType(size.toString(), size.toString()))
              .toList();
    }
    return pageSizeSelectTypes;
  }
}
