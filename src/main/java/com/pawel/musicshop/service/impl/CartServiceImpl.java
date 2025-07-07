package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.Cart;
import com.pawel.musicshop.model.CartItem;
import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.repository.CartItemRepository;
import com.pawel.musicshop.repository.CartRepository;
import com.pawel.musicshop.repository.MusicCDRepository;
import com.pawel.musicshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
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
    @Transactional
    public boolean addMusicCDToCart(String cdId, String userId, int quantity) {
        Optional<MusicCD> musicCD = musicCDRepository.findById(cdId);
        Optional<Cart> cart = cartRepository.findUserCart(userId);
        if(musicCD.isPresent() && cart.isPresent()){
            Optional<CartItem> cartItemOpt = cartItemRepository.findByCartAndCd(cart.get(), musicCD.get());
            if(cartItemOpt.isPresent()){
                if(musicCD.get().getQuantity() - cartItemOpt.get().getQuantity() - quantity < 0){
                    return false;
                }else{
                    CartItem newCartItem = CartItem.builder()
                            .id(UUID.randomUUID().toString())
                            .cart(cart.get())
                            .cd(musicCD.get())
                            .quantity(quantity)
                            .build();
                    cartItemRepository.save(newCartItem);
                    cart.get().getProducts().add(newCartItem);
                    return true;
                }
            }else{
                if(musicCD.get().getQuantity() >= quantity){
                    CartItem newCartItem = CartItem.builder()
                            .id(UUID.randomUUID().toString())
                            .cart(cart.get())
                            .cd(musicCD.get())
                            .quantity(quantity)
                            .build();
                    cartItemRepository.save(newCartItem);
                    cart.get().getProducts().add(newCartItem);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteMusicCDFromCart(String cdId, String userId) {
        Optional<MusicCD> musicCD = musicCDRepository.findById(cdId);
        Optional<Cart> cart = cartRepository.findUserCart(userId);
        if(musicCD.isPresent() && cart.isPresent()){
            Optional<CartItem> cartItem = cartItemRepository.findByCartAndCd(cart.get(), musicCD.get());
            if(cartItem.isPresent()){
                cartItemRepository.deleteById(cartItem.get().getId());
                cart.get().getProducts().remove(cartItem);
                return true;
            }
        }
        return false;
    }

    @Override
    public double getProductsInCartTotalPrice(String id) {
        Optional<Cart> cart = cartRepository.findById(id);
        double result = 0;
        if(cart.isPresent()){
            for(CartItem item : cart.get().getProducts()){
                result += item.getCd().getPrice() * item.getQuantity();
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteCurrentlyUnavailableProducts() {
        List<MusicCD> musicCDs = musicCDRepository.findByIsActiveTrue();
        List<Cart> carts = cartRepository.findAll();
        for(MusicCD cd : musicCDs){
            for(Cart cart : carts){
                for(CartItem cartItem : cart.getProducts()){
                    if(cd.getQuantity() < cartItem.getQuantity()){
                        cart.getProducts().remove(cartItem);
                        cartItemRepository.delete(cartItem);
                        cartRepository.save(cart);
                    }
                }
            }
        }
    }
}
