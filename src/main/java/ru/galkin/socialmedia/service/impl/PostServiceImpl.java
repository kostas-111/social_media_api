package ru.galkin.socialmedia.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.galkin.socialmedia.dto.PostDto;
import ru.galkin.socialmedia.dto.UserPostsDto;
import ru.galkin.socialmedia.entity.Post;
import ru.galkin.socialmedia.entity.PostImage;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.mappers.PostMapper;
import ru.galkin.socialmedia.repository.PostImageRepository;
import ru.galkin.socialmedia.repository.PostRepository;
import ru.galkin.socialmedia.repository.UserRepository;
import ru.galkin.socialmedia.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostImageRepository postImageRepository;
  private final PostMapper postMapper;

  @Override
  @Transactional
  public PostDto createPost(PostDto postDto) {
    User user = userRepository.findById(postDto.getUserId())
        .orElseThrow(() -> new EntityNotFoundException(
            String.format("Пользователь с id: %d не найден", postDto.getUserId()))
        );

    Post post = postMapper.getEntityFromDto(postDto);
    post.setUser(user);
    postRepository.save(post);

    if (postDto.getImagePaths() != null && !postDto.getImagePaths().isEmpty()) {
      List<PostImage> images = postDto.getImagePaths().stream()
          .map(filePath -> {
            PostImage image = new PostImage();
            image.setFilePath(filePath);
            image.setPostId(post.getId());
            return image;
          })
          .collect(Collectors.toList());

      postImageRepository.saveAll(images);
    }
    postDto.setId(post.getId());
    return postDto;
  }

  @Override
  @Transactional
  public boolean updatePost(PostDto postDto) {
    Long postId = postDto.getId();
    Long userId = postDto.getUserId();

    if (!postRepository.existsById(postId)) {
      return false;
    }
    List<Long> userPostIdsList = postRepository.findAllByUserId(userId).stream()
        .map(Post::getId).toList();

    if (!userPostIdsList.contains(postId)) {
      throw new IllegalArgumentException(
          String.format("Пользователь с id: %d не является владельцем поста с id: %d", userId, postId)
      );
    }

    postRepository.updateHeaderAndContent(postDto.getHeader(), postDto.getContent(), postId);
    return true;
  }

  @Override
  @Transactional
  public boolean deletePost(Long postId) {
    if (!postRepository.existsById(postId)) {
      return false;
    }
    postImageRepository.deleteByPostId(postId);
    postRepository.deleteById(postId);
    return true;
  }

  @Override
  public PostDto findById(Long id) {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(String.format("Пост с id: %d не найден", id))
        );
    return postMapper.getDtoFromEntity(post);
  }

  @Override
  @Transactional
  public List<UserPostsDto> findAllPostsByUserIdList(List<Long> userIdList) {
    List<Post> posts = postRepository.findAllByUserIdIn(userIdList);

    Map<Long, List<Post>> postsByUserMap = posts.stream()
        .collect(Collectors.groupingBy(
            post -> post.getUser().getId(),
            Collectors.toList()
        ));

    List<User> users = userRepository.findAllById(userIdList);

    Map<Long, String> userNames = users.stream()
        .collect(Collectors.toMap(
            User::getId,
            User::getName
        ));

    return userIdList.stream()
        .map(userId -> {
          UserPostsDto userPostsDto = new UserPostsDto();
          userPostsDto.setUserId(userId);
          userPostsDto.setName(userNames.getOrDefault(
              userId, String.format("Пользователь с id: %d в системе не зарегистрирован", userId))
          );

          List<PostDto> postDtoList = postsByUserMap.getOrDefault(userId, List.of())
              .stream()
              .map(postMapper::getDtoFromEntity)
              .collect(Collectors.toList());

          userPostsDto.setPosts(postDtoList);
          return userPostsDto;
        })
        .collect(Collectors.toList());
  }
}
