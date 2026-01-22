package ru.galkin.socialmedia.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.galkin.socialmedia.entity.Post;
import ru.galkin.socialmedia.entity.PostImage;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.testtools.Creator;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostImageRepositoryTest {

  @Autowired
  private PostImageRepository postImageRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  private User testUser;
  private Post post1;
  private Post post2;
  private PostImage image1;
  private PostImage image2;
  private PostImage image3;

  @BeforeEach
  void setUp() {
    postImageRepository.deleteAll();
    postRepository.deleteAll();
    userRepository.deleteAll();

    testUser = Creator.createTestUser1();
    userRepository.save(testUser);

    post1 = Creator.createTestPost1(testUser);
    post2 = Creator.createTestPost2(testUser);
    postRepository.saveAll(List.of(post1, post2));

    image1 = Creator.createTestPostImage1(post1);
    image2 = Creator.createTestPostImage2(post2);
    image3 = Creator.createTestPostImage3(post2);
    postImageRepository.saveAll(List.of(image1, image2, image3));
  }

  @AfterEach
  void tearDown() {
    postImageRepository.deleteAll();
    postRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @Transactional
  void whenDeleteByPostIdThenAllImagesForPostAreRemoved() {
    List<PostImage> initialPost1Images = postImageRepository.findAll().stream()
        .filter(img -> img.getPost().getId().equals(post1.getId()))
        .toList();
    assertThat(initialPost1Images).hasSize(1);

    List<PostImage> initialPost2Images = postImageRepository.findAll().stream()
        .filter(img -> img.getPost().getId().equals(post2.getId()))
        .toList();
    assertThat(initialPost2Images).hasSize(2);

    /*
    удаляем изображения второго поста
     */
    postImageRepository.deleteByPostId(post2.getId());
    List<PostImage> remainingImages = postImageRepository.findAll();

    assertThat(remainingImages).hasSize(1);
    assertThat(remainingImages).extracting(PostImage::getFilePath)
            .containsExactlyInAnyOrder("/images/post1/image1.jpg");
    assertThat(remainingImages).extracting(img -> img.getPost().getId())
            .containsOnly(post1.getId());
  }

  @Test
  public void whenSaveImageThenFindById() {
    var foundImage = postImageRepository.findById(image1.getId());
    Assertions.assertThat(foundImage).isPresent();
    Assertions.assertThat(foundImage.get().getFilePath()).isEqualTo("/images/post1/image1.jpg");
  }

  @Test
  public void whenFindAllThenReturnAllImages() {
    List<PostImage> images = postImageRepository.findAll();
    Assertions.assertThat(images.size()).isEqualTo(3);
    Assertions.assertThat(images).extracting(PostImage::getFilePath)
        .contains("/images/post3/image3.jpg",
                  "/images/post2/image2.jpg",
                  "/images/post1/image1.jpg");
  }
}