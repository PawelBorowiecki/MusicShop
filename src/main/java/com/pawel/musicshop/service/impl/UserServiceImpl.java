package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.dto.UserRequest;
import com.pawel.musicshop.model.Role;
import com.pawel.musicshop.model.User;
import com.pawel.musicshop.repository.RoleRepository;
import com.pawel.musicshop.repository.UserRepository;
import com.pawel.musicshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public void register(UserRequest req) {
        //TODO gdy bedzie security
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void deleteById(String id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            user.get().setActive(false);
            userRepository.save(user.get());
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean giveRole(String userId, String roleName) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findByName(roleName);
        if(user.isPresent() && role.isPresent()){
            if(user.get().isActive()){
                user.get().getRoles().add(role.get());
                userRepository.save(user.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public void divestRole(String userId, String roleName) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findByName(roleName);
        if(user.isPresent() && role.isPresent()){
            if(user.get().isActive()){
                user.get().getRoles().remove(role.get());
                userRepository.save(user.get());
            }
        }
    }
}
