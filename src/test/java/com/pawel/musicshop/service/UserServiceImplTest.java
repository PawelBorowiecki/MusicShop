package com.pawel.musicshop.service;

import com.pawel.musicshop.dto.UserRequest;
import com.pawel.musicshop.model.Role;
import com.pawel.musicshop.model.User;
import com.pawel.musicshop.repository.CartItemRepository;
import com.pawel.musicshop.repository.CartRepository;
import com.pawel.musicshop.repository.RoleRepository;
import com.pawel.musicshop.repository.UserRepository;
import com.pawel.musicshop.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_throwsWhenLoginAlreadyExists() {
        when(userRepository.findByLogin("pawel")).thenReturn(Optional.of(User.builder().id("u1").build()));

        UserRequest req = new UserRequest("pawel", "secret");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.register(req));
        assertTrue(ex.getMessage().contains("loginie"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_throwsWhenRoleUserMissing() {
        when(userRepository.findByLogin("pawel")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> userService.register(new UserRequest("pawel", "secret")));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_encodesPasswordAndCreatesCart() {
        Role role = Role.builder().id("r1").name("ROLE_USER").build();
        when(userRepository.findByLogin("pawel")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("secret")).thenReturn("hashed");

        userService.register(new UserRequest("pawel", "secret"));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertEquals("pawel", saved.getLogin());
        assertEquals("hashed", saved.getPassword());
        assertNotEquals("secret", saved.getPassword());
        assertTrue(saved.isActive());
        assertNotNull(saved.getCart());
        verify(cartRepository).save(saved.getCart());
        verify(passwordEncoder).encode("secret");
    }
}
