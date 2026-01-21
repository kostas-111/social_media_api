package ru.galkin.socialmedia.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.galkin.socialmedia.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findAllByUserId(Long userId);

  List<Post> findAllByCreatedBetween(LocalDateTime start, LocalDateTime end);

  Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);

  @Modifying(clearAutomatically = true)
  @Query("""
          UPDATE Post p 
          SET p.header = :header, p.content = :content 
          WHERE p.id = :id
          """)
  void updateHeaderAndContent(@Param("header") String header,
                              @Param("content") String content,
                              @Param("id") Long id);

  @Modifying(clearAutomatically = true)
  @Query("""
          DELETE FROM Post p WHERE p.id = :id
          """)
  void deleteById(@Param("id") Long id);

  @Query("""
SELECT p FROM Post p
JOIN p.user author
JOIN Subscription s ON s.subscriberUser = author
WHERE s.targetUser.id = :userId
ORDER BY p.created DESC
""")
  Page<Post> findAllSubscribersPostsIdOrderByCreatedDesc(@Param("userId") Long userId, Pageable pageable);
}
