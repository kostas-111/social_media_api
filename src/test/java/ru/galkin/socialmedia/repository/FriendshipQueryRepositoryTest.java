package ru.galkin.socialmedia.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.galkin.socialmedia.entity.FriendshipQuery;
import ru.galkin.socialmedia.entity.QueryStatus;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.testtools.Creator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendshipQueryRepositoryTest {

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

  @BeforeEach
  void setUp() {
    friendshipQueryRepository.deleteAll();
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
  }

  @AfterAll
  void tearDown() {
    friendshipQueryRepository.deleteAll();
    queryStatusRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void whenSaveQueryThenFindById() {
    var foundQuery = friendshipQueryRepository.findById(friendshipQuery1.getId());
    assertThat(foundQuery).isPresent();
    assertThat(foundQuery.get().getStatus().getName()).isEqualTo("APPROVED");
  }

  @Test
  public void whenFindAllThenReturnAllQuery() {
    List<FriendshipQuery> friendshipQueries = friendshipQueryRepository.findAll();
    assertThat(friendshipQueries.size()).isEqualTo(2);
    assertThat(friendshipQueries).extracting(FriendshipQuery::getSenderUser).contains(testUser1, testUser2);
  }
}