package ru.galkin.socialmedia.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
    postService.createPost(postDto);
    var uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(postDto.getId())
        .toUri();
    return ResponseEntity.ok().location(uri).body(postDto);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<Post> get(@PathVariable Long postId) {
    return postService.findById(postId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody PostDto postDto) {
    if (postService.updatePost(postDto)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> removeById(@PathVariable Long postId) {
    if (postService.deletePost(postId)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
