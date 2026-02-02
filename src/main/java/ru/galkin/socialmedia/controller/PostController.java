package ru.galkin.socialmedia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "PostController", description = "Контроллер управления API постов")
@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
@Validated
public class PostController {

  private final PostService postService;

  @Operation(
      summary = "Создание нового поста",
      description = "Создает новый пост в системе. Возвращает созданный пользователем пост с присвоенным идентификатором."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostDto.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "409", content = { @Content(schema = @Schema()) })
  })
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

  @Operation(
      summary = "Получить пост по id",
      description = "Получить Пост по его идентификатору. Возвращает объект с id, заголовком, содержанием и путями к файловым вложениям."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostDto.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
  @GetMapping("/{postId}")
  public ResponseEntity<PostDto> get(@Valid
                                  @PathVariable("postId")
                                  @NotNull
                                  @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                  Long postId) {
    PostDto post = postService.findById(postId);
    return ResponseEntity.ok().body(post);
  }

  @Operation(
      summary = "Обновление поста",
      description = "Обновляет заголовок и содержание поста. Возвращает статус операции."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200"),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
  })
  @PutMapping
  public ResponseEntity<Void> update(@Valid @RequestBody PostDto postDto) {
    if (postService.updatePost(postDto)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Удаление поста по id",
      description = "Удаляет пост из системы по его идентификатору. Возвращает статус операции."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204"),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
  })
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

  @Operation(
      summary = "Получить посты по списку пользователей",
      description = "Получить все посты для указанных пользователей. Возвращает список объектов с id пользователя, именем и списком его постов."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserPostsDto.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
  })
  @GetMapping("/users/posts")
  public ResponseEntity<List<UserPostsDto>> getAllUsersPosts(@Valid @RequestParam @NotEmpty
                                                             List<Long> userIdList) {
    List<UserPostsDto> result = postService.findAllPostsByUserIdList(userIdList);
    return ResponseEntity.ok(result);
  }
}
