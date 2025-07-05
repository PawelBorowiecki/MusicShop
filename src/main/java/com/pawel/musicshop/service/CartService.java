package com.pawel.musicshop.service;

import com.pawel.musicshop.model.Cart;

import java.util.List;
import java.util.Optional;

public interface CartService {
    List<Cart> findAll();
    Optional<Cart> findById(String id);
    Optional<Cart> findUserCart(String userId);
    boolean addMusicCDToCart(String cdId, String userId);
    boolean deleteMusicCDFromCart(String cdId, String userId);
}
