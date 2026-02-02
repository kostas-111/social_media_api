package ru.galkin.socialmedia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User's Posts Model Information")
public class UserPostsDto {

  @Schema(description = "Идентификатор пользователя", example = "100")
  private Long userId;

  @Schema(description = "Имя пользователя", example = "Konstantin")
  private String name;

  @Schema(description = "Список постов пользователя")
  private List<PostDto> posts;
}
