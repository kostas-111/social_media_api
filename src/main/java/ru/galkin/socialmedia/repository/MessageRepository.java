package ru.galkin.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galkin.socialmedia.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
