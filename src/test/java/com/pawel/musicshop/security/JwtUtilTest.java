package com.pawel.musicshop.security;

import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails user;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        String secret = Encoders.BASE64URL.encode(
                "01234567890123456789012345678901".getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secret);
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 60_000L);
        user = User.builder()
                .username("pawel")
                .password("x")
                .roles("USER")
                .build();
    }

    @Test
    void generateToken_roundTripsUsernameAndValidates() {
        String token = jwtUtil.generateToken(user);

        assertEquals("pawel", jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.validateToken(token, user));
    }

    @Test
    void validateToken_rejectsWrongUser() {
        String token = jwtUtil.generateToken(user);
        UserDetails other = User.builder()
                .username("other")
                .password("x")
                .roles("USER")
                .build();

        assertFalse(jwtUtil.validateToken(token, other));
    }

    @Test
    void validateToken_rejectsTamperedToken() {
        String token = jwtUtil.generateToken(user);
        String tampered = token.substring(0, token.length() - 4) + "xxxx";

        assertFalse(jwtUtil.validateToken(tampered, user));
    }

    @Test
    void validateToken_rejectsExpiredToken() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 1L);
        String token = jwtUtil.generateToken(user);
        Thread.sleep(20);

        assertFalse(jwtUtil.validateToken(token, user));
    }
}
