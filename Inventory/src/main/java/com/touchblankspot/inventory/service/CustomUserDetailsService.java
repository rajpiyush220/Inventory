package com.touchblankspot.inventory.service;

import com.touchblankspot.inventory.data.model.AuthenticatedUser;
import com.touchblankspot.inventory.data.model.User;
import com.touchblankspot.inventory.data.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetails")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CustomUserDetailsService implements UserDetailsService {

  @NonNull
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    try {
      return new AuthenticatedUser(user);
    } catch (Exception ex) {
      throw new UsernameNotFoundException(username);
    }
  }
}
