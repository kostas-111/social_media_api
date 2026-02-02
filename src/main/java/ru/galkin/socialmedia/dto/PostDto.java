package ru.galkin.socialmedia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Post Model Information")
public class PostDto {

  private Long id;

  @Min(value = 1, message = "Идентификатор пользователя должен быть 1 и более")
  @Schema(description = "Идентификатор пользователя-создателя поста", example = "10")
  private Long userId;

  @NotBlank(message = "Заголовок не может быть пустым")
  @Schema(description = "Заголовок", example = "Интересный пост")
  private String header;

  @NotBlank(message = "Содержание не может быть пустым")
  @Schema(description = "Содержание поста")
  private String content;

  @Schema(description = "Список путей к файловым вложениям")
  private List<String> imagePaths;
}
