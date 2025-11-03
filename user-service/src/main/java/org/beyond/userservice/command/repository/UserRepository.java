package org.beyond.userservice.command.repository;

import java.util.Optional;
import org.beyond.userservice.command.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
