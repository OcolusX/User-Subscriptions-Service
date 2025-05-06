package com.example.user_subscriptions_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.example.user_subscriptions_service.dto.SubscriptionRequest;
import com.example.user_subscriptions_service.dto.SubscriptionResponse;
import com.example.user_subscriptions_service.entity.Subscription;
import com.example.user_subscriptions_service.entity.User;
import com.example.user_subscriptions_service.repository.SubscriptionRepository;
import com.example.user_subscriptions_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock 
    private UserRepository userRepository;
    
    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void testCreateSubscriptionToUser() {
        Long userId = 1L;
        SubscriptionRequest request = new SubscriptionRequest("Netflix");
        User user = new User();
        user.setId(userId);
        
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setTitle("Netflix");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findByTitle("Netflix")).thenReturn(Optional.of(subscription));
        when(userRepository.save(any(User.class))).thenReturn(user);

        SubscriptionResponse response = subscriptionService.addSubscriptionToUser(userId, request);

        assertEquals(1L, (long) response.id());
        assertEquals("Netflix", response.title());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetUserSubscriptions() {
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setTitle("Netflix");

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setSubscriptions(Set.of(subscription));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<SubscriptionResponse> responses = subscriptionService.getUserSubscriptions(userId);

        assertEquals(1, responses.size());
        assertEquals("Netflix", responses.get(0).title());
    }

    @Test
    void testRemoveSubscriptionFromUser() {
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setTitle("Netflix");

        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(subscription);

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setSubscriptions(subscriptions);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));

        subscriptionService.removeSubscriptionFromUser(userId, subscriptionId);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetTopSubscriptions() {
        Subscription subscription1 = new Subscription();
        subscription1.setId(1L);
        subscription1.setTitle("Netflix");
        Subscription subscription2 = new Subscription();
        subscription2.setId(2L);
        subscription2.setTitle("VK");
        Subscription subscription3 = new Subscription();
        subscription3.setId(3L);
        subscription3.setTitle("Yandex");
        List<Subscription> subscriptions = new LinkedList<>();
        subscriptions.add(subscription1);
        subscriptions.add(subscription2);
        subscriptions.add(subscription3);

        when(subscriptionRepository.findTopSubscriptions(PageRequest.of(0, 3))).thenReturn(subscriptions);

        List<SubscriptionResponse> sb = subscriptionService.getTopSubscriptions(3);
        assertEquals(subscription1.getTitle(), sb.get(0).title());
        assertEquals(subscription2.getTitle(), sb.get(1).title());
        assertEquals(subscription3.getTitle(), sb.get(2).title());
    }

}
