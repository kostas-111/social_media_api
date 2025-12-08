package ru.galkin.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galkin.socialmedia.entity.QueryStatus;

public interface QueryStatusRepository extends JpaRepository<QueryStatus, Long> {

}
