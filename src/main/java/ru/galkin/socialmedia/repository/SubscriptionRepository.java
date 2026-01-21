package ru.galkin.socialmedia.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.galkin.socialmedia.entity.Subscription;
import ru.galkin.socialmedia.entity.User;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  @Query("""
          SELECT s.subscriberUser FROM Subscription s 
          WHERE s.targetUser.id = :userId
          """)
  List<User> findAllSubscribersByUserId(@Param("userId") Long userId);
}
