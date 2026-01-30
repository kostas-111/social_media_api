package ru.galkin.socialmedia.service;

import java.util.List;
import ru.galkin.socialmedia.dto.PostDto;
import ru.galkin.socialmedia.dto.UserPostsDto;

public interface PostService {

  PostDto createPost(PostDto postDto);

  boolean updatePost(PostDto postDto);

  PostDto findById(Long id);

  boolean deletePost(Long postId);

  List<UserPostsDto> findAllPostsByUserIdList(List<Long> userIdList);
}
