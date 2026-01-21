package ru.galkin.socialmedia.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.galkin.socialmedia.entity.Subscription;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.testtools.Creator;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubscriptionRepositoryTest {

  @Autowired
  SubscriptionRepository subscriptionRepository;

  @Autowired
  UserRepository userRepository;

  private User testUser1;
  private User testUser2;
  private User testUser3;
  private Subscription subscription1;
  private Subscription subscription2;

  @BeforeEach
  void setUp() {
    subscriptionRepository.deleteAll();
    userRepository.deleteAll();

    testUser1 = Creator.createTestUser1();
    testUser2 = Creator.createTestUser2();
    testUser3 = Creator.createTestUser3();
    userRepository.saveAll(List.of(testUser1, testUser2, testUser3));

    subscription1 = Creator.createTestSubscription(testUser2, testUser1);
    subscription2 = Creator.createTestSubscription(testUser3, testUser1);
    subscriptionRepository.saveAll(List.of(subscription1, subscription2));
  }

  @AfterAll
  void tearDown() {
    subscriptionRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void whenFindAllSubscribersByUserIdThenReturnAllSubscribers() {
    List<User> subscribers = subscriptionRepository.findAllSubscribersByUserId(testUser1.getId());

    /*
    должны получить User2 и User3
     */
    assertThat(subscribers).hasSize(2);
    assertThat(subscribers).extracting(User::getId)
            .containsExactlyInAnyOrder(testUser2.getId(), testUser3.getId());
    assertThat(subscribers).extracting(User::getName)
            .containsExactlyInAnyOrder("Jane Doe", "Jacob Doe");
    assertThat(subscribers).extracting(User::getEmail)
            .containsExactlyInAnyOrder("jane@example.com", "jacob@example.com");
  }

  @Test
  void whenFindAllSubscribersByUserIdWithNoSubscribersThenReturnEmptyList() {
    List<User> subscribers = subscriptionRepository.findAllSubscribersByUserId(testUser2.getId());
    assertThat(subscribers).isEmpty();
  }
}