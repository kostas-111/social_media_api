package ru.galkin.socialmedia.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
  public void createPost(PostDto postDto, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id: %d не найден", userId)));

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
            image.setPost(post);
            return image;
          })
          .collect(Collectors.toList());

      postImageRepository.saveAll(images);
    }
  }

  @Override
  @Transactional
  public void updatePost(PostDto postDto, Long postId, Long userId) {
    List<Long> userPostIdsList = postRepository.findAllByUserId(userId).stream()
        .map(Post::getId).toList();

    if (!userPostIdsList.contains(postId)) {
      throw new IllegalArgumentException(String.format("Пользователь с id: %d не является владельцем поста с id: %d", userId, postId));
    }

    postRepository.updateHeaderAndContent(postDto.getHeader(), postDto.getContent(), postId);
  }

  @Override
  @Transactional
  public void deletePost(Long postId) {
    postImageRepository.deleteByPostId(postId);
    postRepository.deleteById(postId);
  }
}
