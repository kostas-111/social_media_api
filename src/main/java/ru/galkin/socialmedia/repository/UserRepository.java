package ru.galkin.socialmedia.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.galkin.socialmedia.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("""
          SELECT u FROM User u 
          WHERE u.email = :email AND u.password = :password
          """)
  Optional<User> findByEmailAndPassword(@Param("email") String email,
                                        @Param("password") String password);


}
