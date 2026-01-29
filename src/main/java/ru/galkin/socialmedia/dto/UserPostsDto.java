package ru.galkin.socialmedia.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPostsDto {
  private Long userId;
  private String name;
  private List<PostDto> posts;
}
