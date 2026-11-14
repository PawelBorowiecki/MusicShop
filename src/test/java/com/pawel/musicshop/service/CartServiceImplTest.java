package com.pawel.musicshop.service;

import com.pawel.musicshop.model.Cart;
import com.pawel.musicshop.model.CartItem;
import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.repository.CartItemRepository;
import com.pawel.musicshop.repository.CartRepository;
import com.pawel.musicshop.repository.MusicCDRepository;
import com.pawel.musicshop.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private MusicCDRepository musicCDRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private MusicCD cd;
    private Cart cart;

    @BeforeEach
    void setUp() {
        cd = MusicCD.builder()
                .id("cd-1")
                .name("Kind of Blue")
                .price(40.0)
                .quantity(10)
                .isActive(true)
                .build();
        cart = Cart.builder()
                .id("cart-1")
                .products(new HashSet<>())
                .build();
    }

    @Test
    void addMusicCDToCart_savesNewLineWhenStockIsEnough() {
        when(musicCDRepository.findById("cd-1")).thenReturn(Optional.of(cd));
        when(cartRepository.findUserCart("user-1")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndCd(cart, cd)).thenReturn(Optional.empty());

        assertTrue(cartService.addMusicCDToCart("cd-1", "user-1", 2));

        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        verify(cartItemRepository).save(captor.capture());
        assertEquals(2, captor.getValue().getQuantity());
        assertEquals(cd, captor.getValue().getCd());
        assertEquals(1, cart.getProducts().size());
    }

    @Test
    void addMusicCDToCart_rejectsWhenRequestedQuantityExceedsStock() {
        when(musicCDRepository.findById("cd-1")).thenReturn(Optional.of(cd));
        when(cartRepository.findUserCart("user-1")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndCd(cart, cd)).thenReturn(Optional.empty());

        assertFalse(cartService.addMusicCDToCart("cd-1", "user-1", 11));

        verify(cartItemRepository, never()).save(any());
    }

    @Test
    void addMusicCDToCart_incrementsExistingLine() {
        CartItem existing = CartItem.builder()
                .id("item-1")
                .cart(cart)
                .cd(cd)
                .quantity(3)
                .build();
        when(musicCDRepository.findById("cd-1")).thenReturn(Optional.of(cd));
        when(cartRepository.findUserCart("user-1")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndCd(cart, cd)).thenReturn(Optional.of(existing));

        assertTrue(cartService.addMusicCDToCart("cd-1", "user-1", 2));

        verify(cartItemRepository, times(1)).save(existing);
        assertEquals(5, existing.getQuantity());
    }

    @Test
    void addMusicCDToCart_rejectsWhenExistingPlusNewExceedsStock() {
        CartItem existing = CartItem.builder()
                .id("item-1")
                .cart(cart)
                .cd(cd)
                .quantity(9)
                .build();
        when(musicCDRepository.findById("cd-1")).thenReturn(Optional.of(cd));
        when(cartRepository.findUserCart("user-1")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndCd(cart, cd)).thenReturn(Optional.of(existing));

        assertFalse(cartService.addMusicCDToCart("cd-1", "user-1", 2));

        verify(cartItemRepository, never()).save(any());
        assertEquals(9, existing.getQuantity());
    }

    @Test
    void deleteMusicCDFromCart_removesExistingLine() {
        CartItem existing = CartItem.builder()
                .id("item-1")
                .cart(cart)
                .cd(cd)
                .quantity(1)
                .build();
        cart.getProducts().add(existing);
        when(musicCDRepository.findById("cd-1")).thenReturn(Optional.of(cd));
        when(cartRepository.findUserCart("user-1")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndCd(cart, cd)).thenReturn(Optional.of(existing));

        assertTrue(cartService.deleteMusicCDFromCart("cd-1", "user-1"));

        verify(cartItemRepository).deleteById("item-1");
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    void deleteMusicCDFromCart_returnsFalseWhenLineMissing() {
        when(musicCDRepository.findById("cd-1")).thenReturn(Optional.of(cd));
        when(cartRepository.findUserCart("user-1")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndCd(cart, cd)).thenReturn(Optional.empty());

        assertFalse(cartService.deleteMusicCDFromCart("cd-1", "user-1"));

        verify(cartItemRepository, never()).deleteById(any());
    }

    @Test
    void getProductsInCartTotalPrice_sumsPriceTimesQuantity() {
        MusicCD other = MusicCD.builder().id("cd-2").price(10.0).quantity(5).build();
        cart.setProducts(Set.of(
                CartItem.builder().id("i1").cd(cd).quantity(2).build(),
                CartItem.builder().id("i2").cd(other).quantity(3).build()
        ));
        when(cartRepository.findById("cart-1")).thenReturn(Optional.of(cart));

        assertEquals(110.0, cartService.getProductsInCartTotalPrice("cart-1"));
    }

    @Test
    void getProductsInCartTotalPrice_returnsZeroWhenCartMissing() {
        when(cartRepository.findById("missing")).thenReturn(Optional.empty());

        assertEquals(0.0, cartService.getProductsInCartTotalPrice("missing"));
    }
}
