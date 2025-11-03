package org.beyond.userservice.query.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.beyond.userservice.query.dto.UserDTO;

@Mapper
public interface UserMapper {

  UserDTO findUserByUsername(String username);

  List<UserDTO> findAllUsers();
}
