package ru.galkin.socialmedia.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.galkin.socialmedia.entity.Post;
import ru.galkin.socialmedia.entity.Subscription;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.testtools.Creator;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest {

  @Autowired
  PostRepository postRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  SubscriptionRepository subscriptionRepository;

  private User testUser1;
  private User testUser2;
  private User testUser3;
  private Post post1;
  private Post post2;
  private Post post3;

  @BeforeEach
  void setUp() {
    subscriptionRepository.deleteAll();
    postRepository.deleteAll();
    userRepository.deleteAll();

    testUser1 = Creator.createTestUser1();
    testUser2 = Creator.createTestUser2();
    testUser3 = Creator.createTestUser3();
    userRepository.saveAll(List.of(testUser1, testUser2, testUser3));

    post1 = Creator.createTestPost1(testUser1);
    post2 = Creator.createTestPost2(testUser2);
    post3 = Creator.createTestPost3(testUser2);

    postRepository.saveAll(List.of(post1, post2, post3));
  }

  @AfterEach
  void tearDown() {
    subscriptionRepository.deleteAll();
    postRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void whenFindAllByUserIdThenReturnUserPosts() {
    List<Post> user1Posts = postRepository.findAllByUserId(testUser1.getId());
    List<Post> user2Posts = postRepository.findAllByUserId(testUser2.getId());

    assertThat(user1Posts).hasSize(1);
    assertThat(user1Posts).extracting(Post::getHeader)
            .containsExactlyInAnyOrder("First Post");
    assertThat(user2Posts).hasSize(2);
    assertThat(user2Posts.get(0).getHeader()).isEqualTo("Second Post");
    assertThat(user2Posts.get(1).getHeader()).isEqualTo("Third Post");
  }

  @Test
  public void whenFindAllByCreatedBetweenThenReturnPostsInDateRange() {
    LocalDateTime start = LocalDateTime.now().minusDays(3);
    LocalDateTime end = LocalDateTime.now().minusHours(12);

    List<Post> posts = postRepository.findAllByCreatedBetween(start, end);

    assertThat(posts).hasSize(2);
    assertThat(posts).extracting(Post::getHeader)
            .containsExactlyInAnyOrder("First Post", "Second Post");
  }

  @Test
  public void whenFindAllByOrderByCreatedDescThenReturnPostsSorted() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("created").descending());

    Page<Post> postsPage = postRepository.findAllByOrderByCreatedDesc(pageable);
    List<Post> posts = postsPage.getContent();

    /*
    Проверяем сортировку от новых к старым
     */
    assertThat(posts).hasSize(3);
    assertThat(posts.get(0).getHeader()).isEqualTo("Third Post");
    assertThat(posts.get(1).getHeader()).isEqualTo("Second Post");
    assertThat(posts.get(2).getHeader()).isEqualTo("First Post");

  }

  @Test
  @Transactional
  public void whenUpdateHeaderAndContentThenPostIsUpdated() {
    String newHeader = "Updated Header";
    String newContent = "Updated Content";

    postRepository.updateHeaderAndContent(newHeader, newContent, post1.getId());
    Post updatedPost = postRepository.findById(post1.getId()).orElseThrow();

    assertThat(updatedPost.getHeader()).isEqualTo(newHeader);
    assertThat(updatedPost.getContent()).isEqualTo(newContent);

    /*
    Проверяем, что другие посты не изменились
     */
    Post unchangedPost = postRepository.findById(post2.getId()).orElseThrow();
    assertThat(unchangedPost.getHeader()).isEqualTo("Second Post");
  }

  @Test
  public void whenDeleteByIdThenPostIsRemoved() {
    Long postId = post1.getId();
    postRepository.deleteById(postId);

    assertThat(postRepository.findById(postId)).isEmpty();
    assertThat(postRepository.findAll()).hasSize(2);
    assertThat(postRepository.findAll()).extracting(Post::getHeader)
            .containsExactlyInAnyOrder("Second Post", "Third Post");
  }

  @Test
  public void whenFindAllSubscribersPostsThenReturnPostsFromSubscribers() {
    /*
    User2 и User3 подписаны на User1
     */
    Subscription subscription1 = new Subscription();
    subscription1.setSubscriberUser(testUser2);
    subscription1.setTargetUser(testUser1);

    Subscription subscription2 = new Subscription();
    subscription2.setSubscriberUser(testUser3);
    subscription2.setTargetUser(testUser1);

    subscriptionRepository.saveAll(List.of(subscription1, subscription2));

    /*
    Создаем пост от User3
     */
    Post post4 = Creator.createTestPost4(testUser3);
    postRepository.save(post4);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("created").descending());

    /*
    Получаем посты подписчиков User1
     */
    Page<Post> postsPage = postRepository.findAllSubscribersPostsIdOrderByCreatedDesc(
        testUser1.getId(), pageable);

    /*
    Должны получить посты только от User2 и User3
     */
    List<Post> posts = postsPage.getContent();
    assertThat(posts).hasSize(3);
    assertThat(posts).extracting(Post::getHeader)
            .containsExactlyInAnyOrder("Fourth Post", "Third Post", "Second Post");
    assertThat(posts).extracting(p -> p.getUser().getId())
            .containsExactlyInAnyOrder(testUser3.getId(), testUser2.getId(), testUser2.getId());
  }

  @Test
  public void whenFindAllSubscribersPostsNoSubscribersThenReturnEmptyPage() {
    Pageable pageable = PageRequest.of(0, 10);

    Page<Post> postsPage = postRepository.findAllSubscribersPostsIdOrderByCreatedDesc(
        testUser2.getId(), pageable);

    assertThat(postsPage.getContent()).isEmpty();
    assertThat(postsPage.getTotalElements()).isZero();
  }
}