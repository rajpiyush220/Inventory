package com.touchblankspot.inventory.portal.web;

public interface WebConstant {

  String PASSWORD_RESET_ENDPOINT = "/changePassword";

  String[] PERMIT_ALL_URL = {
    "/css/**",
    "/js/**",
    "/images/**",
    "/register/super/admin",
    "/resetPassword",
    "/changePassword",
    "/savePassword",
    "/download"
  };

  String LOGIN_PAGE_ENDPOINT = "/login";

  String LOGIN_SUCCESS_ENDPOINT = "/welcome";

  String DEFAULT_SUCCESS_URL = "/welcome";

  String LOGIN_FAIL_ENDPOINT = LOGIN_PAGE_ENDPOINT + "?error";
}
