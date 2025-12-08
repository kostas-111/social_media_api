package ru.galkin.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galkin.socialmedia.entity.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
