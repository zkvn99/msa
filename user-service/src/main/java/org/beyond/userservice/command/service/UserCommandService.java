package org.beyond.userservice.command.service;

import lombok.RequiredArgsConstructor;
import org.beyond.userservice.command.dto.UserCreateRequest;
import org.beyond.userservice.command.entity.User;
import org.beyond.userservice.command.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  @Transactional
  public void registerUser(UserCreateRequest request) {
    // 중복 회원 체크 로직 등 로직 필요
    User user = modelMapper.map(request, User.class);
    user.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
    userRepository.save(user);
  }
}
