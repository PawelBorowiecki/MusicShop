package com.pawel.musicshop.service;

import com.pawel.musicshop.dto.UserRequest;
import com.pawel.musicshop.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void register(UserRequest req);
    Optional<User> findById(String id);
    Optional<User> findByLogin(String login);
    void deleteById(String id);
    List<User> getUsers();
    boolean giveRole(String userId, String roleName);
    void divestRole(String userId, String roleName);
}
