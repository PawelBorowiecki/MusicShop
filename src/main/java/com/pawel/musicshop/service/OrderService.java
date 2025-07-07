package com.pawel.musicshop.service;

import com.pawel.musicshop.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAll();
    Optional<Order> findById(String id);
    List<Order> findUserOrders(String userId);
    boolean placeOrder(String userId);
    boolean changeOrderStatus(String id, String newStatus);
    double getTotalPrice(String id);
}
