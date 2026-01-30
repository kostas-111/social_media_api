package ru.galkin.socialmedia.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.galkin.socialmedia.dto.PostDto;
import ru.galkin.socialmedia.entity.Post;
import ru.galkin.socialmedia.entity.PostImage;

@Mapper(componentModel = "spring")
public interface PostMapper {

  @Mapping(target="id", source="id")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target="header", source="header")
  @Mapping(target="content", source="content")
  @Mapping(target="imagePaths", source="images")
  PostDto getDtoFromEntity(Post post);

  @Mapping(target="id", source="id")
  @Mapping(target="header", source="header")
  @Mapping(target="content", source="content")
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "images", ignore = true)
  Post getEntityFromDto(PostDto postDto);

  default List<String> mapImages(List<PostImage> images) {
    if (images == null) {
      return List.of();
    }
    return images.stream()
        .map(PostImage::getFilePath)
        .toList();
  }
}
