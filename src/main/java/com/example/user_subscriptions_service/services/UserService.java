package com.example.user_subscriptions_service.services;

import org.springframework.stereotype.Service;

import com.example.user_subscriptions_service.dto.UserRequest;
import com.example.user_subscriptions_service.dto.UserResponse;
import com.example.user_subscriptions_service.entity.User;
import com.example.user_subscriptions_service.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest request) {
        log.info("Creating user with name {}, email: {}", request.name(), request.email());
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user = userRepository.save(user);
        log.debug("User created with ID: {}", user.getId());
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserResponse getUser(Long id) {
        log.info("Getting user with ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("User not found with ID: {}", id);
                return new EntityNotFoundException("User not found");
            });
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("User not found with ID: {}", id);
                return new EntityNotFoundException("User not found");
            });
        user.setName(request.name());
        user.setEmail(request.email());
        user = userRepository.save(user);
        log.debug("User updated with ID: {}", user.getId());
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if(!userRepository.existsById(id)) {
            log.warn("User not found with ID: {}", id);
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        log.debug("User was deleted with ID: {}", id);
    }
}
