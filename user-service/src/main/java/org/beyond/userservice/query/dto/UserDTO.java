package org.beyond.userservice.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
  private String username;
  private String role;
  // 추가 개인 정보
}
