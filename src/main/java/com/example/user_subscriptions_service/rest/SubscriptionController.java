package com.example.user_subscriptions_service.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_subscriptions_service.dto.SubscriptionRequest;
import com.example.user_subscriptions_service.dto.SubscriptionResponse;
import com.example.user_subscriptions_service.services.SubscriptionService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{id}/subscriptions")
    public ResponseEntity<SubscriptionResponse> addSubscription(@PathVariable Long id, @RequestBody SubscriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.addSubscriptionToUser(id, request));
    }

    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getUserSubscriptions(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(id));
    }

    @DeleteMapping("/{id}/subscriptions/{sub_id}")
    public ResponseEntity<Void> removeSubscription(@PathVariable Long id, @PathVariable("sub_id") Long subId) {
        subscriptionService.removeSubscriptionFromUser(id, subId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/subscriptions/top{limit}")
    public ResponseEntity<List<SubscriptionResponse>> getMethodName(@RequestParam(defaultValue = "3") Integer limit) {
        return ResponseEntity.ok(subscriptionService.getTopSubscriptions(limit));
    }
    

}
