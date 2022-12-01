package com.touchblankspot.inventory.portal.web.util;

import static com.touchblankspot.inventory.portal.web.WebConstant.PASSWORD_RESET_ENDPOINT;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WebUtil {

  private static final String password_reset_url_format = "%s%s?token=%s";

  public static String getAppUrl(HttpServletRequest request) {
    return "http://"
        + request.getServerName()
        + ":"
        + request.getServerPort()
        + request.getContextPath();
  }

  public static String getPasswordResetUrl(HttpServletRequest request, UUID token) {
    return String.format(
        password_reset_url_format, getAppUrl(request), PASSWORD_RESET_ENDPOINT, token.toString());
  }
}
