package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.Cart;
import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.repository.CartRepository;
import com.pawel.musicshop.repository.MusicCDRepository;
import com.pawel.musicshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final MusicCDRepository musicCDRepository;
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

    @Override
    public boolean addMusicCDToCart(String cdId, String userId) {
        Optional<MusicCD> musicCD = musicCDRepository.findById(cdId);
        Optional<Cart> cart = cartRepository.findUserCart(userId);
        if(musicCD.isPresent() && cart.isPresent()){
            if(musicCD.get().getCart() != null || !musicCD.get().isActive()){
                return false;
            }else{
                cart.get().getProducts().add(musicCD.get());
                musicCD.get().setCart(cart.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteMusicCDFromCart(String cdId, String userId) {
        Optional<MusicCD> musicCD = musicCDRepository.findById(cdId);
        Optional<Cart> cart = cartRepository.findUserCart(userId);
        if(musicCD.isPresent() && cart.isPresent()){
            if(musicCD.get().getCart() != null && cart.get().getProducts().contains(musicCD.get())){
                musicCD.get().setCart(cart.get());
                cart.get().getProducts().remove(musicCD.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public double getProductsInCartTotalPrice(String id) {
        Optional<Cart> cart = cartRepository.findById(id);
        return cart.map(value -> value.getProducts().stream().mapToDouble(MusicCD::getPrice).sum()).orElse(-1.0);
    }
}
