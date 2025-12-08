package ru.galkin.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galkin.socialmedia.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
