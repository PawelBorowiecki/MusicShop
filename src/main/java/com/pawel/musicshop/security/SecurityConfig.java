package com.pawel.musicshop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter,
            AuthenticationProvider authProvider
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/").hasRole("ADMIN")
                        .requestMatchers("/api/musicCD/all").hasRole("ADMIN")
                        .requestMatchers("/api/musicCD/allActive").hasRole("ADMIN")
                        .requestMatchers("/api/musicCD/allInCarts").hasRole("ADMIN")
                        .requestMatchers("/api/musicCD/get={id}").hasRole("ADMIN")
                        .requestMatchers("/api/musicCD/allAvailable").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/musicCD/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/musicCD/*").hasRole("ADMIN")
                        .requestMatchers("/api/cart/all").hasRole("ADMIN")
                        .requestMatchers("/api/cart/get={id}").hasRole("ADMIN")
                        .requestMatchers("api/cart/user").hasRole("USER")
                        .requestMatchers("api/cart/price").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "api/cart/").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "api/cart/").hasRole("USER")
                        .requestMatchers("api/orders/all").hasRole("ADMIN")
                        .requestMatchers("api/orders/get={id}").hasRole("ADMIN")
                        .requestMatchers("api/orders/user").hasRole("USER")
                        .requestMatchers("api/orders/place").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "api/orders/").hasRole("ADMIN")
                        .requestMatchers("/api/payments/checkout/{orderId}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/payments/webhook").permitAll()
                        .requestMatchers("/api/payments/success", "/api/payments/cancel").permitAll()
                        .requestMatchers("/error", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
