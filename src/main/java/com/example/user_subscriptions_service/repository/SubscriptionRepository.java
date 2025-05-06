package com.example.user_subscriptions_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.user_subscriptions_service.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByTitle(String title);

    @Query("SELECT s FROM Subscription s LEFT JOIN s.users u GROUP BY s ORDER BY COUNT(u) DESC")
    List<Subscription> findTopSubscriptions(Pageable pageable);

}
