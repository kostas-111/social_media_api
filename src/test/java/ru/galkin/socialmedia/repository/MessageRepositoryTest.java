package ru.galkin.socialmedia.repository;

import java.time.LocalDateTime;
import java.util.List;
import static ru.galkin.socialmedia.testtools.Creator.createTestUser1;
import static ru.galkin.socialmedia.testtools.Creator.createTestUser2;
import static ru.galkin.socialmedia.testtools.Creator.createTestUser3;
import static ru.galkin.socialmedia.testtools.Creator.createTestMessage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.galkin.socialmedia.entity.Message;
import ru.galkin.socialmedia.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  private User testUser1;
  private User testUser2;
  private User testUser3;
  private Message message1;
  private Message message2;
  private Message message3;

  @BeforeEach
  void setUp() {
    messageRepository.deleteAll();
    userRepository.deleteAll();

    testUser1 = createTestUser1();
    testUser2 = createTestUser2();
    testUser3 = createTestUser3();
    userRepository.saveAll(List.of(testUser1, testUser2, testUser3));

    message1 = createTestMessage(testUser1, testUser2, "Привет! Как дела?",
        LocalDateTime.now().minusHours(2));
    message2 = createTestMessage(testUser2, testUser1, "Привет! Все хорошо, спасибо!",
        LocalDateTime.now().minusHours(1));
    message3 = createTestMessage(testUser1, testUser3, "Привет, как твои успехи?",
        LocalDateTime.now().minusMinutes(30));
    messageRepository.saveAll(List.of(message1, message2, message3));
  }

  @AfterAll
  void tearDown() {
    messageRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void whenSaveMessageThenFindById() {
    var foundMessage = messageRepository.findById(message1.getId());
    assertThat(foundMessage).isPresent();
    assertThat(foundMessage.get().getContent()).isEqualTo("Привет! Как дела?");
    assertThat(foundMessage.get().getSenderUser()).isEqualTo(testUser1);
    assertThat(foundMessage.get().getReceiverUser()).isEqualTo(testUser2);
  }

  @Test
  void whenFindAllThenReturnAllMessages() {
    List<Message> messages = messageRepository.findAll();
    assertThat(messages).hasSize(3);
    assertThat(messages).extracting(Message::getContent)
        .containsExactlyInAnyOrder(
            "Привет! Как дела?",
            "Привет! Все хорошо, спасибо!",
            "Привет, как твои успехи?"
        );
  }
}