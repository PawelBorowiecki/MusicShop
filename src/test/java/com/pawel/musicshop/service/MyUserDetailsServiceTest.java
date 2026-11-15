package com.pawel.musicshop.service;

import com.pawel.musicshop.model.Role;
import com.pawel.musicshop.model.User;
import com.pawel.musicshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_mapsRolesAndEnabledFlag() {
        Role admin = Role.builder().id("r1").name("ROLE_ADMIN").build();
        User user = User.builder()
                .id("u1")
                .login("admin")
                .password("hash")
                .isActive(true)
                .roles(Set.of(admin))
                .build();
        when(userRepository.findByLogin("admin")).thenReturn(Optional.of(user));

        UserDetails details = userDetailsService.loadUserByUsername("admin");

        assertEquals("admin", details.getUsername());
        assertEquals("hash", details.getPassword());
        assertTrue(details.isEnabled());
        Set<String> authorities = details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertEquals(Set.of("ROLE_ADMIN"), authorities);
    }

    @Test
    void loadUserByUsername_disablesInactiveUser() {
        User user = User.builder()
                .id("u1")
                .login("gone")
                .password("hash")
                .isActive(false)
                .roles(Set.of())
                .build();
        when(userRepository.findByLogin("gone")).thenReturn(Optional.of(user));

        UserDetails details = userDetailsService.loadUserByUsername("gone");

        assertFalse(details.isEnabled());
    }

    @Test
    void loadUserByUsername_throwsWhenMissing() {
        when(userRepository.findByLogin("nobody")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nobody"));
    }
}
