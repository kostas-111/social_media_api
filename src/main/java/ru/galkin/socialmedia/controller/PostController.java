package ru.galkin.socialmedia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.galkin.socialmedia.dto.PostDto;
import ru.galkin.socialmedia.dto.UserPostsDto;
import ru.galkin.socialmedia.service.PostService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
@Validated
public class PostController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<PostDto> save(@Valid @RequestBody PostDto postDto) {
    PostDto newPost = postService.createPost(postDto);
    var uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(newPost.getId())
        .toUri();
    return ResponseEntity.ok().location(uri).body(newPost);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<PostDto> get(@Valid
                                  @PathVariable("postId")
                                  @NotNull
                                  @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                  Long postId) {
    PostDto post = postService.findById(postId);
    return ResponseEntity.ok().body(post);
  }

  @PutMapping
  public ResponseEntity<Void> update(@Valid @RequestBody PostDto postDto) {
    if (postService.updatePost(postDto)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> removeById(@Valid
                                         @PathVariable("postId")
                                         @NotNull
                                         @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                         Long postId) {
    if (postService.deletePost(postId)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/users/posts")
  public ResponseEntity<List<UserPostsDto>> getAllUsersPosts(@Valid @RequestParam @NotEmpty
                                                             List<Long> userIdList) {
    List<UserPostsDto> result = postService.findAllPostsByUserIdList(userIdList);
    return ResponseEntity.ok(result);
  }
}
