package ru.galkin.socialmedia.service;

public interface SubscribeService {

  void createSubscription(Long senderId, Long receiverId);

  void deleteSubscription(Long subscriberId, Long targetUserId);
}
