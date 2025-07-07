package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.*;
import com.pawel.musicshop.repository.*;
import com.pawel.musicshop.service.CartService;
import com.pawel.musicshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final MusicCDRepository musicCDRepository;
    private final CartService cartService;
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findUserOrders(String userId) {
        return orderRepository.findUserOrders(userId);
    }

    @Override
    @Transactional
    public Order placeOrder(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            Order order = Order.builder()
                    .id(UUID.randomUUID().toString())
                    .user(user.get())
                    .creationDate(LocalDateTime.now())
                    .status(OrderStatus.CREATED)
                    .build();

            orderRepository.save(order);
            Set<OrderItem> orderItemSet = new HashSet<>();

            for(CartItem p : user.get().getCart().getProducts()){
                OrderItem orderItem = OrderItem.builder()
                        .id(UUID.randomUUID().toString())
                        .order(order)
                        .cd(p.getCd())
                        .quantity(p.getQuantity())
                        .price(p.getCd().getPrice() * p.getQuantity())
                        .build();
                orderItemSet.add(orderItem);
                orderItemRepository.save(orderItem);
                cartItemRepository.delete(p);
                Optional<MusicCD> cd = musicCDRepository.findById(p.getCd().getId());
                if(cd.isPresent()){
                    cd.get().setQuantity(cd.get().getQuantity() - p.getQuantity());
                    musicCDRepository.save(cd.get());
                }
            }

            order.setItems(orderItemSet);
            orderRepository.save(order);
            cartService.deleteCurrentlyUnavailableProducts();

            return order;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean changeOrderStatus(String id, String newStatus) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()){
            order.get().setStatus(OrderStatus.valueOf(newStatus));
            return true;
        }
        return false;
    }

    @Override
    public double getTotalPrice(String id) {
        double result = 0;
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()){
            for(OrderItem orderItem : order.get().getItems()){
                result += orderItem.getPrice();
            }
        }
        return result;
    }
}
