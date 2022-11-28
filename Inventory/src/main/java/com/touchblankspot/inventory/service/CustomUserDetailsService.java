package com.touchblankspot.inventory.service;

import com.touchblankspot.inventory.data.repository.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetails")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CustomUserDetailsService implements UserDetailsService {

  @NonNull private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    com.touchblankspot.inventory.data.model.User user = userRepository.findByUserName(username);
    if (user == null) throw new UsernameNotFoundException(username);
    Set<GrantedAuthority> grantedAuthorities =
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());
    try {
      return new User(user.getUserName(), user.getPassword(), grantedAuthorities);
    } catch (Exception ex) {
      throw new UsernameNotFoundException(username);
    }
  }
}
