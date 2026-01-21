package ru.galkin.socialmedia.testtools;

import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;
import ru.galkin.socialmedia.entity.Post;
import ru.galkin.socialmedia.entity.PostImage;
import ru.galkin.socialmedia.entity.Subscription;
import ru.galkin.socialmedia.entity.User;

@UtilityClass
public class Creator {

  public static User createTestUser1() {
    User testUser1 = new User();
    testUser1.setName("John Doe");
    testUser1.setEmail("john@example.com");
    testUser1.setPassword("password123");
    return testUser1;
  }

  public static User createTestUser2() {
    User testUser2 = new User();
    testUser2.setName("Jane Doe");
    testUser2.setEmail("jane@example.com");
    testUser2.setPassword("password456");
    return testUser2;
  }

  public static User createTestUser3() {
    User testUser3 = new User();
    testUser3.setName("Jacob Doe");
    testUser3.setEmail("jacob@example.com");
    testUser3.setPassword("password789");
    return testUser3;
  }

  public static Post createTestPost1(User user) {
    Post post = new Post();
    post.setHeader("First Post");
    post.setContent("Content of first post");
    post.setCreated(LocalDateTime.now().minusDays(2));
    post.setUser(user);
    return post;
  }

  public static Post createTestPost2(User user) {
    Post post = new Post();
    post.setHeader("Second Post");
    post.setContent("Content of second post");
    post.setCreated(LocalDateTime.now().minusDays(1));
    post.setUser(user);
    return post;
  }

  public static Post createTestPost3(User user) {
    Post post = new Post();
    post.setHeader("Third Post");
    post.setContent("Content of third post");
    post.setCreated(LocalDateTime.now());
    post.setUser(user);
    return post;
  }

  public static Post createTestPost4(User user) {
    Post post = new Post();
    post.setHeader("Fourth Post");
    post.setContent("Content of fourth post");
    post.setCreated(LocalDateTime.now().plusHours(1));
    post.setUser(user);
    return post;
  }

  public static PostImage createTestPostImage1(Post post) {
    PostImage postImage = new PostImage();
    postImage.setFilePath("/images/post1/image1.jpg");
    postImage.setPost(post);
    return postImage;
  }

  public static PostImage createTestPostImage2(Post post) {
    PostImage postImage = new PostImage();
    postImage.setFilePath("/images/post2/image2.jpg");
    postImage.setPost(post);
    return postImage;
  }

  public static PostImage createTestPostImage3(Post post) {
    PostImage postImage = new PostImage();
    postImage.setFilePath("/images/post3/image3.jpg");
    postImage.setPost(post);
    return postImage;
  }

  public static Subscription createTestSubscription(User subscriber, User target) {
    Subscription subscription = new Subscription();
    subscription.setSubscriberUser(subscriber);
    subscription.setTargetUser(target);
    return subscription;
  }
}
