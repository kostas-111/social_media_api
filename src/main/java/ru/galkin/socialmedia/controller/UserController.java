package ru.galkin.socialmedia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "UserController", description = "Контроллер управления API пользовтелей")
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

  private final UserService userService;

  @Operation(
      summary = "Создание нового пользователя",
      description = "Создает нового пользователя в системе. Возвращает созданного пользователя с присвоенным идентификатором."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "409", content = { @Content(schema = @Schema()) })
  })
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

  @Operation(
      summary = "Получить пользователя по id",
      description = "Получить Пользователя по его идентификатору. Возвращает объект с id, именем, почтой и паролем."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> get(@Valid
                                     @PathVariable("userId")
                                     @NotNull
                                     @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                     Long userId) {
    UserDto user = userService.findUserById(userId);
    return ResponseEntity.ok().body(user);
  }

  @Operation(
      summary = "Обновление данных пользователя",
      description = "Обновляет данные существующего пользователя. Возвращает статус операции."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200"),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
  })
  @PutMapping
  public ResponseEntity<Void> update(@RequestBody UserDto userDto) {
    if (userService.updateUser(userDto)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Удаление пользователя по id",
      description = "Удаляет пользователя из системы по его идентификатору. Возвращает статус операции."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204"),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
  })
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
