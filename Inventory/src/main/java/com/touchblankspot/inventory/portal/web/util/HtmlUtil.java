package com.touchblankspot.inventory.portal.web.util;

import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HtmlUtil {

  private static final String LIST_ITEM_FORMAT = "<li value='%s'>%s</li>";

  public static String buildListItem(List<String> strings) {
    return strings.stream()
        .map(string -> String.format(LIST_ITEM_FORMAT, string, string))
        .collect(Collectors.joining(" "));
  }
}
