package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.Cart;
import com.pawel.musicshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public Optional<Cart> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Cart> findUserCart(String userId) {
        return null;
    }
}
