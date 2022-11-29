package com.touchblankspot.inventory.web;

public interface WebConstant {

  String PASSWORD_RESET_ENDPOINT = "/changePassword";

  String[] PERMIT_ALL_URL = {
    "/css/**", "/js/**", "/registration", "/resetPassword", "/changePassword", "/savePassword"
  };

  String LOGIN_PAGE_ENDPOINT = "/login";

  String LOGIN_SUCCESS_ENDPOINT = "/welcome";

  String LOGIN_FAIL_ENDPOINT = LOGIN_PAGE_ENDPOINT + "?error";
}
