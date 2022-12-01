package com.touchblankspot.inventory.portal.security.service;

import com.touchblankspot.inventory.portal.data.model.AuthenticatedUser;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class SecurityService {

  @NonNull
  private final AuthenticationManager authenticationManager;

  @NonNull
  private final UserDetailsService userDetailsService;

  public Boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
      return false;
    }
    return authentication.isAuthenticated();
  }

  public void autoLogin(String username, String password) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(
            userDetails, password, userDetails.getAuthorities());

    authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    if (usernamePasswordAuthenticationToken.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      log.debug(String.format("Auto login %s successfully!", username));
    }
  }

  public List<String> getCurrentUserRoles() {
    isAuthenticated();
    AuthenticatedUser authenticatedUser =
        (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return authenticatedUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
  }
}
