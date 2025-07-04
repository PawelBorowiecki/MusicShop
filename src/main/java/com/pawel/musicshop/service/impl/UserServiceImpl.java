package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.dto.UserRequest;
import com.pawel.musicshop.model.User;
import com.pawel.musicshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public void register(UserRequest req) {

    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public boolean giveRole(String userId, String roleName) {
        return false;
    }

    @Override
    public void divestRole(String userId, String roleName) {

    }
}
