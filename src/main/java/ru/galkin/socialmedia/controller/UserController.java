package ru.galkin.socialmedia.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.service.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<User> save(@RequestBody User user) {
    userService.saveUser(user);
    var uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(user.getId())
        .toUri();
    return ResponseEntity.ok().location(uri).body(user);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> get(@PathVariable Long userId) {
    return userService.findUserById(userId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody User user) {
    if (userService.updateUser(user)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> removeById(@PathVariable Long userId) {
    if (userService.deleteUserById(userId)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
