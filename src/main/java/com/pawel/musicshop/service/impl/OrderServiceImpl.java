package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.Order;
import com.pawel.musicshop.repository.OrderRepository;
import com.pawel.musicshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
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
}
