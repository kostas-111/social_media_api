package ru.galkin.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.galkin.socialmedia.entity.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

  @Modifying(clearAutomatically = true)
  @Query("""
          DELETE FROM PostImage pi WHERE pi.post.id = :postId
          """)
  void deleteByPostId(@Param("postId") Long postId);
}
