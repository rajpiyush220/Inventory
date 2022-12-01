package com.touchblankspot.inventory.service;

import com.touchblankspot.common.validator.FieldValueExists;
import com.touchblankspot.inventory.constant.RoleEnum;
import com.touchblankspot.inventory.data.model.PasswordResetToken;
import com.touchblankspot.inventory.data.model.User;
import com.touchblankspot.inventory.data.repository.PasswordTokenRepository;
import com.touchblankspot.inventory.data.repository.RoleRepository;
import com.touchblankspot.inventory.data.repository.UserRepository;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserService implements FieldValueExists {

  @Value("${password.reset.token.validity_in_minute:15}")
  private Integer resetTokenValidityPeriod;

  @NonNull
  private final UserRepository userRepository;

  @NonNull
  private final RoleRepository roleRepository;

  @NonNull
  private final PasswordTokenRepository passwordTokenRepository;

  @NonNull
  private final PasswordEncoder passwordEncoder;

  public User findByUserName(String username) {
    return userRepository.findByUserName(username);
  }

  public void save(User user, String roleName) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(Set.of(roleRepository.findByName(roleName)));
    userRepository.save(user);
  }

  @Override
  public boolean fieldValueExists(Object value, String fieldName)
      throws UnsupportedOperationException {
    if ("username".equals(fieldName.toLowerCase())) {
      return userRepository.findByUserName(value.toString()) != null;
    }
    throw new UnsupportedOperationException("Operation not supported for " + fieldName);
  }

  public void createPasswordResetTokenForUser(User user, UUID token) {
    PasswordResetToken myToken =
        new PasswordResetToken(
            token, user, OffsetDateTime.now().plusMinutes(resetTokenValidityPeriod));
    passwordTokenRepository.save(myToken);
  }

  public String validatePasswordResetToken(String token) {
    final PasswordResetToken passwordResetToken =
        passwordTokenRepository.findByToken(UUID.fromString(token));
    return passwordResetToken == null
        ? "Invalid token."
        : isTokenExpired(passwordResetToken)
        ? "Your registration token has expired. Please register again."
        : null;
  }

  public Optional<User> getUserByPasswordResetToken(final String token) {
    return Optional.ofNullable(
        passwordTokenRepository.findByToken(UUID.fromString(token)).getUser());
  }

  public void changeUserPassword(User user, String password) {
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
  }

  public Boolean isSuperAdminExists() {
    return userRepository.isSuperAdminExists();
  }

  private boolean isTokenExpired(PasswordResetToken passwordResetToken) {
    return passwordResetToken.getExpirationTime().compareTo(OffsetDateTime.now()) < 0;
  }
}
