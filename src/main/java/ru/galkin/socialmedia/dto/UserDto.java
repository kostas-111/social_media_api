package ru.galkin.socialmedia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Long id;

  @NotBlank(message = "Имя пользователя не может быть пустым")
  private String name;

  @NotBlank(message = "Адрес почты не может быть пустым")
  private String email;

  @NotBlank(message = "Пароль не может быть пустым")
  private String password;
}
