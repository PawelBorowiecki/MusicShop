package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.Order;
import com.pawel.musicshop.model.OrderStatus;
import com.pawel.musicshop.model.User;
import com.pawel.musicshop.repository.OrderRepository;
import com.pawel.musicshop.repository.UserRepository;
import com.pawel.musicshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
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
                    .products(user.get().getCart().getProducts())
                    .build();
            orderRepository.save(order);
            return true;
        }

        return false;
    }

    @Override
    public boolean changeOrderStatus(String id, String newStatus) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()){
            order.get().setStatus(OrderStatus.valueOf(newStatus));
            return true;
        }
        return false;
    }
}
