package com.touchblankspot.inventory.security.handler;

import static com.touchblankspot.inventory.constant.RoleEnum.ADMIN;
import static com.touchblankspot.inventory.constant.RoleEnum.MANAGER;
import static com.touchblankspot.inventory.constant.RoleEnum.STAFF;
import static com.touchblankspot.inventory.constant.RoleEnum.SUPERVISOR;
import static com.touchblankspot.inventory.constant.RoleEnum.SUPER_ADMIN;
import static com.touchblankspot.inventory.constant.RoleEnum.USER;
import static com.touchblankspot.inventory.web.WebConstant.DEFAULT_SUCCESS_URL;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private static RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  private static final Map<String, String> roleTargetUrlMap = Map.of(
      SUPER_ADMIN.name(), DEFAULT_SUCCESS_URL,
      ADMIN.name(), DEFAULT_SUCCESS_URL,
      MANAGER.name(), DEFAULT_SUCCESS_URL,
      SUPERVISOR.name(), DEFAULT_SUCCESS_URL,
      USER.name(), DEFAULT_SUCCESS_URL,
      STAFF.name(), DEFAULT_SUCCESS_URL
  );

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication)
      throws IOException, ServletException {
    handle(request, response, authentication);
    clearAuthenticationAttributes(request);
  }

  protected void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws IOException {

    String targetUrl = determineTargetUrl(authentication);

    if (response.isCommitted()) {
      log.debug(
          "Response has already been committed. Unable to redirect to "
              + targetUrl);
      return;
    }
    redirectStrategy.sendRedirect(request, response, targetUrl);
  }

  protected String determineTargetUrl(final Authentication authentication) {
    for (String authorityName : authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).toList()) {
      if (roleTargetUrlMap.containsKey(authorityName)) {
        return roleTargetUrlMap.get(authorityName);
      }
    }
    throw new IllegalStateException();
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }
}
