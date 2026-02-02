package ru.galkin.socialmedia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Model Information")
public class UserDto {

  @Schema(description = "Идентификатор пользователя", example = "100")
  private Long id;

  @NotBlank(message = "Имя пользователя не может быть пустым")
  @Schema(description = "Имя пользователя", example = "Konstantin")
  private String name;

  @NotBlank(message = "Адрес почты не может быть пустым")
  @Schema(description = "Адрес электронной почты пользователя", example = "konstantin@mail.com")
  private String email;

  @NotBlank(message = "Пароль не может быть пустым")
  @Schema(description = "Пароль", example = "12345")
  private String password;
}
