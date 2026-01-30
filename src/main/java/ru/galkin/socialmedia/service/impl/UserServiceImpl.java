package ru.galkin.socialmedia.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.galkin.socialmedia.dto.UserDto;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.mappers.UserMapper;
import ru.galkin.socialmedia.repository.UserRepository;
import ru.galkin.socialmedia.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto saveUser(UserDto userDto) {
    User newUser = userRepository.save(userMapper.getEntityFromDto(userDto));
    return userMapper.getDtoFromEntity(newUser);
  }

  @Override
  public UserDto findUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            String.format("Пользователь с id: %d не найден", id))
        );
    return userMapper.getDtoFromEntity(user);
  }

  @Override
  public boolean updateUser(UserDto userDto) {
    User user = userMapper.getEntityFromDto(userDto);
    if (!userRepository.existsById(user.getId())) {
      return false;
    }
    userRepository.save(user);
    return true;
  }

  @Override
  public boolean deleteUserById(Long id) {
    if (!userRepository.existsById(id)) {
      return false;
    }
    userRepository.deleteById(id);
    return true;
  }
}
