package ru.galkin.socialmedia.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
  private Long id;
  private Long userId;
  private String header;
  private String content;
  private List<String> imagePaths;
}
