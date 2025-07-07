package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.*;
import com.pawel.musicshop.repository.CartItemRepository;
import com.pawel.musicshop.repository.OrderItemRepository;
import com.pawel.musicshop.repository.OrderRepository;
import com.pawel.musicshop.repository.UserRepository;
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
    public boolean placeOrder(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            Order order = Order.builder()
                    .id(UUID.randomUUID().toString())
                    .user(user.get())
                    .creationDate(LocalDateTime.now())
                    .status(OrderStatus.CREATED)
                    .build();

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
            }

            order.setItems(orderItemSet);
            orderRepository.save(order);
            return true;
        }

        return false;
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
