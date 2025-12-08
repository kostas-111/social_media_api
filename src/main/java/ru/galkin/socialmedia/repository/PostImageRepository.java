package ru.galkin.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galkin.socialmedia.entity.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

}
