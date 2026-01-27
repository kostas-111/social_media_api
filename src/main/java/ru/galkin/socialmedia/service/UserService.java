package ru.galkin.socialmedia.service;

import java.util.Optional;
import ru.galkin.socialmedia.entity.User;

public interface UserService {

  User saveUser(User user);

  Optional<User> findUserById(Long id);

  void deleteUserById(Long id);
}
