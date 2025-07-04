package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.Order;
import com.pawel.musicshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Order> findUserOrders(String userId) {
        return null;
    }
}
