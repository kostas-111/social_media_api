package ru.galkin.socialmedia.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.galkin.socialmedia.dto.PostDto;
import ru.galkin.socialmedia.entity.Post;
import ru.galkin.socialmedia.entity.PostImage;
import ru.galkin.socialmedia.entity.User;
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

  @Override
  @Transactional
  public PostDto createPost(PostDto postDto) {
    User user = userRepository.findById(postDto.getUserId())
        .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id: %d не найден", postDto.getUserId())));

    Post post = new Post();
    post.setUser(user);
    post.setHeader(postDto.getHeader());
    post.setContent(postDto.getContent());

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
      throw new IllegalArgumentException(String.format("Пользователь с id: %d не является владельцем поста с id: %d", userId, postId));
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
  public Optional<Post> findById(Long id) {
    return postRepository.findById(id);
  }
}
