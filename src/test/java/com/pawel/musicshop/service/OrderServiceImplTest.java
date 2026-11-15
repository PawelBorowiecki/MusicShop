package com.pawel.musicshop.service;

import com.pawel.musicshop.model.*;
import com.pawel.musicshop.repository.*;
import com.pawel.musicshop.service.impl.OrderServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private MusicCDRepository musicCDRepository;
    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void placeOrder_copiesCartLinesDecrementsStockAndClearsCart() {
        MusicCD cd = MusicCD.builder()
                .id("cd-1")
                .price(25.0)
                .quantity(8)
                .build();
        CartItem cartItem = CartItem.builder()
                .id("ci-1")
                .cd(cd)
                .quantity(3)
                .build();
        Cart cart = Cart.builder()
                .id("cart-1")
                .products(new HashSet<>(Set.of(cartItem)))
                .build();
        User user = User.builder()
                .id("user-1")
                .login("pawel")
                .cart(cart)
                .build();
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(musicCDRepository.findById("cd-1")).thenReturn(Optional.of(cd));

        Order result = orderService.placeOrder("user-1");

        assertNotNull(result);
        assertEquals(OrderStatus.CREATED, result.getStatus());
        assertEquals(user, result.getUser());
        verify(orderRepository, atLeastOnce()).save(any(Order.class));

        ArgumentCaptor<OrderItem> itemCaptor = ArgumentCaptor.forClass(OrderItem.class);
        verify(orderItemRepository).save(itemCaptor.capture());
        assertEquals(3, itemCaptor.getValue().getQuantity());
        assertEquals(75.0, itemCaptor.getValue().getPrice());
        verify(cartItemRepository).delete(cartItem);
        assertEquals(5, cd.getQuantity());
        verify(musicCDRepository).save(cd);
        verify(cartService).deleteCurrentlyUnavailableProducts();
    }

    @Test
    void placeOrder_returnsNullWhenUserMissing() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        assertNull(orderService.placeOrder("missing"));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void getTotalPrice_sumsOrderItemPrices() {
        Order order = Order.builder()
                .id("order-1")
                .items(Set.of(
                        OrderItem.builder().id("oi-1").price(40.0).build(),
                        OrderItem.builder().id("oi-2").price(15.5).build()
                ))
                .build();
        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));

        assertEquals(55.5, orderService.getTotalPrice("order-1"));
    }

    @Test
    void changeOrderStatus_setsValidEnum() {
        Order order = Order.builder()
                .id("order-1")
                .status(OrderStatus.CREATED)
                .build();
        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));

        assertTrue(orderService.changeOrderStatus("order-1", "SENT"));
        assertEquals(OrderStatus.SENT, order.getStatus());
    }

    @Test
    void changeOrderStatus_throwsOnInvalidEnum() {
        Order order = Order.builder()
                .id("order-1")
                .status(OrderStatus.CREATED)
                .build();
        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));

        assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus("order-1", "NOT_A_STATUS"));
    }
}
