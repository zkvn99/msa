package org.beyond.userservice.auth.service;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.beyond.userservice.command.entity.User;
import org.beyond.userservice.command.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
                              .orElseThrow(() -> new UsernameNotFoundException("유저 찾지 못함"));

    // Spring Security Context에 담길 수 있는 User 타입으로 반환 (필요하다면 상속해서 추가 속성 확장 가능)
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
    );
  }
}
