package ru.galkin.socialmedia.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.galkin.socialmedia.entity.Friend;
import ru.galkin.socialmedia.entity.User;

public interface FriendRepository extends JpaRepository<Friend, Long> {

  @Query("""
          SELECT f.secondUser FROM Friend f 
          JOIN f.friendshipQuery fq 
          JOIN fq.status s 
          WHERE f.firstUser.id = :userId 
          AND s.name = 'APPROVED'
          UNION
          SELECT f.firstUser FROM Friend f 
          JOIN f.friendshipQuery fq 
          JOIN fq.status s 
          WHERE f.secondUser.id = :userId 
          AND s.name = 'APPROVED'
          """)
  List<User> findAllFriendsByUserId(@Param("userId") Long userId);
}
