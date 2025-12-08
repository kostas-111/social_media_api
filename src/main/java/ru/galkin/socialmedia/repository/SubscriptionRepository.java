package ru.galkin.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galkin.socialmedia.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}
