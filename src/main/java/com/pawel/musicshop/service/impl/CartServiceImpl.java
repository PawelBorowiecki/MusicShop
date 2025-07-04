package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.Cart;
import com.pawel.musicshop.repository.CartRepository;
import com.pawel.musicshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Optional<Cart> findById(String id) {
        return cartRepository.findById(id);
    }

    @Override
    public Optional<Cart> findUserCart(String userId) {
        return cartRepository.findUserCart(userId);
    }
}
