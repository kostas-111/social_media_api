package ru.galkin.socialmedia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.galkin.socialmedia.dto.UserDto;
import ru.galkin.socialmedia.service.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
    UserDto newUser = userService.saveUser(userDto);
    var uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(newUser.getId())
        .toUri();
    return ResponseEntity.ok().location(uri).body(newUser);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> get(@Valid
                                     @PathVariable("userId")
                                     @NotNull
                                     @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                     Long userId) {
    UserDto user = userService.findUserById(userId);
    return ResponseEntity.ok().body(user);
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody UserDto userDto) {
    if (userService.updateUser(userDto)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> removeById(@Valid
                                         @PathVariable("userId")
                                         @NotNull
                                         @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                         Long userId) {
    if (userService.deleteUserById(userId)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
