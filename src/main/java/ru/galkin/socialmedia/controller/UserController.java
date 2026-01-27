package ru.galkin.socialmedia.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.service.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<User> save(@RequestBody User user) {
    return ResponseEntity.ok(userService.saveUser(user));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> get(@PathVariable Long userId) {
    return userService.findUserById(userId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public void update(@RequestBody User user) {
    userService.saveUser(user);
  }

  @DeleteMapping("/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeById(@PathVariable Long userId) {
    userService.deleteUserById(userId);
  }

}
