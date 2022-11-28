package com.touchblankspot.inventory.service;

import com.touchblankspot.common.validator.FieldValueExists;
import com.touchblankspot.inventory.data.model.User;
import com.touchblankspot.inventory.data.repository.RoleRepository;
import com.touchblankspot.inventory.data.repository.UserRepository;
import java.util.HashSet;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserService implements FieldValueExists {

  @NonNull private final UserRepository userRepository;

  @NonNull private final RoleRepository roleRepository;

  @NonNull private final PasswordEncoder passwordEncoder;

  public User findByUserName(String username) {
    return userRepository.findByUserName(username);
  }

  public void save(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(new HashSet<>(roleRepository.findAll()));
    userRepository.save(user);
  }

  @Override
  public boolean fieldValueExists(Object value, String fieldName)
      throws UnsupportedOperationException {
    if ("username".equals(fieldName.toLowerCase())) {
      return userRepository.findByUserName(value.toString()) != null;
    }
    throw new UnsupportedOperationException("Operation not suppported for " + fieldName);
  }
}
