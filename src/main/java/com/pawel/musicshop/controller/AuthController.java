package com.pawel.musicshop.controller;

import com.pawel.musicshop.dto.LoginRequest;
import com.pawel.musicshop.dto.LoginResponse;
import com.pawel.musicshop.dto.UserRequest;
import com.pawel.musicshop.model.User;
import com.pawel.musicshop.security.JwtUtil;
import com.pawel.musicshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        Optional<User> user = userService.findByLogin(loginRequest.getLogin());
        if(user.isPresent()){
            if(user.get().isActive()){
                Authentication auth;
                try {
                    auth = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
                    );
                } catch (BadCredentialsException e){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                String token = jwtUtil.generateToken(userDetails);
                LoginResponse responseBody = new LoginResponse(token);
                return ResponseEntity.ok(responseBody);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest req) {
        try {
            userService.register(req);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Registered successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }
}
