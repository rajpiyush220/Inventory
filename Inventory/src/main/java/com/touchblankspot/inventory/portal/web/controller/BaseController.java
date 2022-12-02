package com.touchblankspot.inventory.portal.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseController {

  @Value("${application.pagination.pageSizeList}")
  public List<Integer> pageSizeList;
}
