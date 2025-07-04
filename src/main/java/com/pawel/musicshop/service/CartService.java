package com.pawel.musicshop.service;

import com.pawel.musicshop.model.Cart;

import java.util.List;
import java.util.Optional;

public interface CartService {
    List<Cart> findAll();
    Optional<Cart> findById(String id);
    List<Cart> findUserCart(String userId);
}
