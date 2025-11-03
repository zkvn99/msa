package org.beyond.userservice.query.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.beyond.userservice.query.dto.UserDTO;
import org.beyond.userservice.query.dto.UserDetailResponse;
import org.beyond.userservice.query.dto.UserListResponse;
import org.beyond.userservice.query.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {

  private final UserMapper userMapper;

  public UserDetailResponse getUserDetail(String username) {
    UserDTO user = Optional.ofNullable(
        userMapper.findUserByUsername(username)
    ).orElseThrow(() -> new RuntimeException("유저 정보 찾지 못함"));

    return UserDetailResponse.builder()
        .user(user).build();
  }

  public UserListResponse getAllUsers() {
    List<UserDTO> users = userMapper.findAllUsers();
    return UserListResponse.builder()
        .users(users)
        .build();
  }
}
