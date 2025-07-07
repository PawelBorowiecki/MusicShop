package com.pawel.musicshop.repository;

import com.pawel.musicshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
    Optional<Cart> findUserCart(String userId);
}
