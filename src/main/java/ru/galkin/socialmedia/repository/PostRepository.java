package ru.galkin.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galkin.socialmedia.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
