package com.pawel.musicshop.repository;

import com.pawel.musicshop.model.Cart;
import com.pawel.musicshop.model.CartItem;
import com.pawel.musicshop.model.MusicCD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCartAndCd(Cart cart, MusicCD cd);
}
