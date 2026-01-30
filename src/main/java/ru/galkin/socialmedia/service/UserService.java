package ru.galkin.socialmedia.service;

import ru.galkin.socialmedia.dto.UserDto;

public interface UserService {

  UserDto saveUser(UserDto user);

  UserDto findUserById(Long id);

  boolean updateUser(UserDto userDto);

  boolean deleteUserById(Long id);
}
