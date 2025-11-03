package org.beyond.userservice.auth.repository;

import org.beyond.userservice.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

}
