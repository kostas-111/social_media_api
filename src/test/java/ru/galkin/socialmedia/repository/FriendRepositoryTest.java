package ru.galkin.socialmedia.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.galkin.socialmedia.entity.Friend;
import ru.galkin.socialmedia.entity.FriendshipQuery;
import ru.galkin.socialmedia.entity.QueryStatus;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.testtools.Creator;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendRepositoryTest {

  @Autowired
  private FriendRepository friendRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FriendshipQueryRepository friendshipQueryRepository;

  @Autowired
  private QueryStatusRepository queryStatusRepository;

  private User testUser1;
  private User testUser2;
  private User testUser3;
  private QueryStatus approvedStatus;
  private FriendshipQuery friendshipQuery1;
  private FriendshipQuery friendshipQuery2;
  private Friend friend1;
  private Friend friend2;

  @BeforeEach
  void setUp() {
    friendRepository.deleteAll();
    friendshipQueryRepository.deleteAll();
    queryStatusRepository.deleteAll();
    userRepository.deleteAll();

    testUser1 = Creator.createTestUser1();
    testUser2 = Creator.createTestUser2();
    testUser3 = Creator.createTestUser3();
    userRepository.saveAll(List.of(testUser1, testUser2, testUser3));

    approvedStatus = new QueryStatus();
    approvedStatus.setName("APPROVED");
    approvedStatus = queryStatusRepository.save(approvedStatus);

    friendshipQuery1 = new FriendshipQuery();
    friendshipQuery1.setSenderUser(testUser1);
    friendshipQuery1.setReceiverUser(testUser2);
    friendshipQuery1.setQueryDate(LocalDateTime.now().minusDays(2));
    friendshipQuery1.setStatus(approvedStatus);

    friendshipQuery2 = new FriendshipQuery();
    friendshipQuery2.setSenderUser(testUser2);
    friendshipQuery2.setReceiverUser(testUser3);
    friendshipQuery2.setQueryDate(LocalDateTime.now().minusDays(1));
    friendshipQuery2.setStatus(approvedStatus);

    friendshipQueryRepository.saveAll(List.of(friendshipQuery1, friendshipQuery2));

    friend1 = new Friend();
    friend1.setFirstUser(testUser1);
    friend1.setSecondUser(testUser2);
    friend1.setBeginDate(LocalDateTime.now().minusDays(2));
    friend1.setFriendshipQuery(friendshipQuery1);

    friend2 = new Friend();
    friend2.setFirstUser(testUser2);
    friend2.setSecondUser(testUser3);
    friend2.setBeginDate(LocalDateTime.now().minusDays(1));
    friend2.setFriendshipQuery(friendshipQuery2);

    friendRepository.saveAll(List.of(friend1, friend2));
  }

  @AfterAll
  void tearDown() {
    friendRepository.deleteAll();
    friendshipQueryRepository.deleteAll();
    queryStatusRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void whenFindAllFriendsByUserIdThenReturnApprovedFriends() {
    /*
    User2 дружит с User1 и User3
     */
    List<User> user2Friends = friendRepository.findAllFriendsByUserId(testUser2.getId());

    assertThat(user2Friends).hasSize(2);
    assertThat(user2Friends).extracting(User::getId)
            .containsExactlyInAnyOrder(testUser1.getId(), testUser3.getId());
    assertThat(user2Friends).extracting(User::getName)
            .containsExactlyInAnyOrder(testUser1.getName(), testUser3.getName());

    /*
    Друзья User1 (должен быть только User2)
     */
    List<User> user1Friends = friendRepository.findAllFriendsByUserId(testUser1.getId());
    assertThat(user1Friends).hasSize(1);
    assertThat(user1Friends.get(0).getId()).isEqualTo(testUser2.getId());
    assertThat(user1Friends.get(0).getName()).isEqualTo(testUser2.getName());

    /*
    Друзья User3 (должен быть только User2)
     */
    List<User> user3Friends = friendRepository.findAllFriendsByUserId(testUser3.getId());
    assertThat(user3Friends).hasSize(1);
    assertThat(user3Friends.get(0).getId()).isEqualTo(testUser2.getId());
  }
}