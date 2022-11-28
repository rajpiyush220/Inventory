package com.touchblankspot.inventory.data.repository;

import com.touchblankspot.inventory.data.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  User findByUserName(String username);
}
