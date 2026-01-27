package ru.galkin.socialmedia.service;

import java.util.Optional;
import ru.galkin.socialmedia.dto.PostDto;
import ru.galkin.socialmedia.entity.Post;

public interface PostService {

  PostDto createPost(PostDto postDto);

  PostDto updatePost(PostDto postDto);

  Optional<Post> findById(Long id);

  void deletePost(Long postId);

}
