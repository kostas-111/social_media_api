package ru.galkin.socialmedia.service;

import ru.galkin.socialmedia.dto.PostDto;

public interface PostService {

  void createPost(PostDto postDto, Long userId);

  void updatePost(PostDto postDto, Long postId, Long userId);

  void deletePost(Long postId);

}
