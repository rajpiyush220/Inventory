package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.PasswordResetToken;
import com.touchblankspot.inventory.portal.data.model.User;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

  PasswordResetToken findByToken(UUID token);

  PasswordResetToken findByUser(User user);

  Stream<PasswordResetToken> findAllByExpirationTimeLessThan(OffsetDateTime now);

  void deleteByExpirationTimeLessThan(OffsetDateTime now);

  @Modifying
  @Query(
      nativeQuery = true,
      value = "delete from password_reset_token where expired_at < current_time()")
  void deleteAllExpired();
}
