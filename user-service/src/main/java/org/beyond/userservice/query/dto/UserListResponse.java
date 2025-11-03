package org.beyond.userservice.query.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserListResponse {
  private List<UserDTO> users;
}
