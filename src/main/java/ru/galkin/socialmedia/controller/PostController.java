package ru.galkin.socialmedia.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.galkin.socialmedia.dto.PostDto;
import ru.galkin.socialmedia.entity.Post;
import ru.galkin.socialmedia.service.PostService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<PostDto> save(@RequestBody PostDto postDto) {
    return ResponseEntity.ok(postService.createPost(postDto));
  }

  @GetMapping("/{postId}")
  public ResponseEntity<Post> get(@PathVariable Long postId) {
    return postService.findById(postId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public PostDto update(@RequestBody PostDto postDto) {
    postService.updatePost(postDto);
    return postDto;
  }

  @DeleteMapping("/{postId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeById(@PathVariable Long postId) {
    postService.deletePost(postId);
  }
}
