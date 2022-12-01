package com.touchblankspot.inventory.data.repository;

import com.touchblankspot.inventory.data.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  User findByUserName(String username);

  @Query(nativeQuery = true, value = """
      select
      	case when count(*) > 0 then 'true' else 'false' end
      from user
      	inner join user_roles on user.id = user_roles.users_id
        inner join role on role.id = user_roles.roles_id and role.name = 'SUPER_ADMIN'
      """)
  public Boolean isSuperAdminExists();
}
