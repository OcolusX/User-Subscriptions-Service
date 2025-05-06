package com.example.user_subscriptions_service.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.user_subscriptions_service.dto.SubscriptionRequest;
import com.example.user_subscriptions_service.dto.SubscriptionResponse;
import com.example.user_subscriptions_service.entity.Subscription;
import com.example.user_subscriptions_service.entity.User;
import com.example.user_subscriptions_service.repository.SubscriptionRepository;
import com.example.user_subscriptions_service.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionResponse addSubscriptionToUser(Long userId, SubscriptionRequest request) {
        log.info("Adding subscriptions {} to user with ID ", request.title(), userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User not found with ID: {}", userId);
                return new EntityNotFoundException("User not found");
            });
        
        log.info("Getting subscription with title: {}", request.title());
        Subscription subscription = subscriptionRepository.findByTitle(request.title())
            .orElseGet(() -> {
                log.warn("Subscription not found with title: {}", request.title());
                log.debug("Creating subscription with title: {}", request.title());
                Subscription newSubscription = new Subscription();
                newSubscription.setTitle(request.title());
                return subscriptionRepository.save(newSubscription);
            });

        user.getSubscriptions().add(subscription);
        userRepository.save(user);
        log.debug("Subscription {} added to user with ID: {}", subscription, user.getId());

        return new SubscriptionResponse(subscription.getId(), subscription.getTitle());
    }

    public List<SubscriptionResponse> getUserSubscriptions(Long userId) {
        log.info("Getting subscriptions for user with ID: {}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User not found with ID: {}", userId);
                return new EntityNotFoundException("User not found");
            });
        
        return user.getSubscriptions().stream()
            .map(s -> new SubscriptionResponse(s.getId(), s.getTitle()))
            .toList();
    }

    public void removeSubscriptionFromUser(Long userId, Long subscriptionId) {
        log.info("Removing subscription with ID {} from user with ID {}", subscriptionId, userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User not found with ID: {}", userId);
                return new EntityNotFoundException("User not found");
            });

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> {
                log.warn("Subscription not found with ID: {}", subscriptionId);
                return new EntityNotFoundException("Subscription not found");
            });

        user.getSubscriptions().remove(subscription);
        userRepository.save(user);
        log.debug("Subscription was removed with ID: {} from user with ID: {}", subscriptionId, userId);
    }

    public List<SubscriptionResponse> getTopSubscriptions(Integer limit) {
        log.info("Fetching top subscriptions with count: {}", limit);
        return subscriptionRepository.findTopSubscriptions(PageRequest.of(0, limit)).stream()
            .map(s -> new SubscriptionResponse(s.getId(), s.getTitle()))
            .toList();
    }

}
