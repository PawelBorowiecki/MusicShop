package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.Cart;
import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.model.User;
import com.pawel.musicshop.repository.CartRepository;
import com.pawel.musicshop.repository.MusicCDRepository;
import com.pawel.musicshop.repository.UserRepository;
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
    private final UserRepository userRepository;
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
            if(musicCD.get().isInCart()){
                return false;
            }else{
                cart.get().getProducts().add(musicCD.get());
                musicCD.get().setInCart(true);
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
            if(musicCD.get().isInCart() && cart.get().getProducts().contains(musicCD.get())){
                musicCD.get().setInCart(false);
                cart.get().getProducts().remove(musicCD.get());
                return true;
            }
        }
        return false;
    }
}
