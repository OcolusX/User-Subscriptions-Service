package com.example.user_subscriptions_service.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.user_subscriptions_service.dto.UserRequest;
import com.example.user_subscriptions_service.dto.UserResponse;
import com.example.user_subscriptions_service.entity.User;
import com.example.user_subscriptions_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock 
    private UserRepository userRepository;

    @InjectMocks 
    private UserService userService;

    @Test
    void testCreateUser() {
        UserRequest request = new UserRequest("Dmitry", "dmitry@example.com");
        User user = new User();
        user.setId(1L);
        user.setName("Dmitry");
        user.setEmail("dmitry@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(request);

        assertEquals(1L, (long)response.id());
        assertEquals("Dmitry", response.name());
        assertEquals("dmitry@example.com", response.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Dmitry");
        user.setEmail("dmitry@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUser(1L);

        assertEquals(1L, (long)response.id());
        assertEquals("Dmitry", response.name());
        assertEquals("dmitry@example.com", response.email());
    }

    @Test
    void testUpdateUser() {
        UserRequest request = new UserRequest("Alice", "alice@example.com");
        User user = new User();
        user.setId(1L);
        user.setName("Dmitry");
        user.setEmail("dmitry@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserResponse response = userService.updateUser(1L, request);

        assertEquals(1L, (long)response.id());
        assertEquals("Alice", response.name());
        assertEquals("alice@example.com", response.email());
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

}
