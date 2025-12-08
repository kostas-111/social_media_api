package ru.galkin.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galkin.socialmedia.entity.FriendshipQuery;

public interface FriendshipQueryRepository extends JpaRepository<FriendshipQuery, Long> {

}
