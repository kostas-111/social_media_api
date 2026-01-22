package ru.galkin.socialmedia.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.galkin.socialmedia.entity.PostImage;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.testtools.Creator;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private User testUser1;
  private User testUser2;
  private User testUser3;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();

    testUser1 = Creator.createTestUser1();
    testUser2 = Creator.createTestUser2();
    testUser3 = Creator.createTestUser3();

    userRepository.saveAll(List.of(testUser1, testUser2, testUser3));
  }

  @AfterAll
  void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  void whenFindByEmailAndPasswordWithCorrectCredentialsThenReturnUser() {
    String correctEmail = testUser1.getEmail();
    String correctPassword = testUser1.getPassword();

    Optional<User> foundUser = userRepository.findByEmailAndPassword(correctEmail, correctPassword);

    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getId()).isEqualTo(testUser1.getId());
    assertThat(foundUser.get().getName()).isEqualTo(testUser1.getName());
    assertThat(foundUser.get().getEmail()).isEqualTo(correctEmail);
  }

  @Test
  void whenFindByEmailAndPasswordWithWrongPasswordThenReturnEmpty() {
    String correctEmail = testUser2.getEmail();
    String wrongPassword = "wrong_password";

    Optional<User> foundUser = userRepository.findByEmailAndPassword(correctEmail, wrongPassword);

    assertThat(foundUser).isEmpty();
  }

  @Test
  void whenFindByEmailAndPasswordWithWrongEmailThenReturnEmpty() {
    String wrongEmail = "wrong@example.com";
    String correctPassword = testUser3.getPassword();

    Optional<User> foundUser = userRepository.findByEmailAndPassword(wrongEmail, correctPassword);

    assertThat(foundUser).isEmpty();
  }

  @Test
  void whenFindByEmailAndPasswordWithBothWrongThenReturnEmpty() {
    String wrongEmail = "wrong@example.com";
    String wrongPassword = "wrong_password";

    Optional<User> foundUser = userRepository.findByEmailAndPassword(wrongEmail, wrongPassword);

    assertThat(foundUser).isEmpty();
  }

  @Test
  public void whenSaveUserThenFindById() {
    var foundUser = userRepository.findById(testUser1.getId());
    Assertions.assertThat(foundUser).isPresent();
    Assertions.assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");
  }

  @Test
  public void whenFindAllThenReturnAllUsers() {
    List<User> users = userRepository.findAll();
    Assertions.assertThat(users.size()).isEqualTo(3);
    Assertions.assertThat(users).extracting(User::getName)
        .contains("John Doe", "Jane Doe", "Jacob Doe");
  }
}