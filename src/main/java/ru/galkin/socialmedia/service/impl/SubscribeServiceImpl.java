package ru.galkin.socialmedia.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.galkin.socialmedia.entity.FriendshipQuery;
import ru.galkin.socialmedia.entity.Subscription;
import ru.galkin.socialmedia.entity.User;
import ru.galkin.socialmedia.repository.FriendshipQueryRepository;
import ru.galkin.socialmedia.repository.SubscriptionRepository;
import ru.galkin.socialmedia.repository.UserRepository;
import ru.galkin.socialmedia.service.SubscribeService;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;
  private final FriendshipQueryRepository friendshipQueryRepository;

  @Override
  @Transactional
  public void createSubscription(Long senderId, Long receiverId) {
    Subscription senderSubscription = new Subscription();
    Subscription receiverSubscription = new Subscription();

    User subscriberUser = userRepository.findById(senderId)
        .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id: %d не найден", senderId)));
    User targetUser = userRepository.findById(receiverId)
        .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id: %d не найден", receiverId)));

    Optional<FriendshipQuery> friendshipQuery = friendshipQueryRepository.findBySenderUserId(senderId);

    if (friendshipQuery.isPresent() && "SEND".equals(friendshipQuery.get().getStatus().getName())) {
      senderSubscription.setSubscriberUser(subscriberUser);
      senderSubscription.setTargetUser(targetUser);
      subscriptionRepository.save(senderSubscription);
    }

    if (friendshipQuery.isPresent() && "APPROVED".equals(friendshipQuery.get().getStatus().getName())) {
      receiverSubscription.setSubscriberUser(targetUser);
      receiverSubscription.setTargetUser(subscriberUser);
      subscriptionRepository.save(receiverSubscription);
    }
  }

  @Override
  @Transactional
  public void deleteSubscription(Long subscriberId, Long targetUserId) {
    Subscription subscriptionForDelete =
        subscriptionRepository.findBySubscriberUserIdAndTargetUserId(subscriberId, targetUserId)
            .orElseThrow(() -> new EntityNotFoundException(
                String.format("Для пользователя с id: %d не найден подписчик с id: %d", targetUserId, subscriberId)));
    subscriptionRepository.delete(subscriptionForDelete);
  }
}
