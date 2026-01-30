package ru.galkin.socialmedia.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

  private Long id;

  @Min(value = 1, message = "Идентификатор пользователя должен быть 1 и более")
  private Long userId;

  @NotBlank(message = "Заголовок не может быть пустым")
  private String header;

  @NotBlank(message = "Содержание не может быть пустым")
  private String content;

  private List<String> imagePaths;
}
