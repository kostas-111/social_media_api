package ru.galkin.socialmedia.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.repository.UserRepository;
import ru.galkin.socialmedia.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public void deleteUserById(Long id) {
    userRepository.deleteById(id);
  }
}
