package com.pawel.musicshop.controller;

import com.pawel.musicshop.model.Order;
import com.pawel.musicshop.model.User;
import com.pawel.musicshop.service.OrderService;
import com.pawel.musicshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<Order> getAllOrders(){
        return orderService.findAll();
    }

    @GetMapping("/get={id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id){
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("user")
    public ResponseEntity<?> getUserOrders(@AuthenticationPrincipal UserDetails userDetails){
        String login = userDetails.getUsername();
        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()){
            return ResponseEntity.ok(orderService.findUserOrders(user.get().getId()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("place")
    public ResponseEntity<?> placeOrder(@AuthenticationPrincipal UserDetails userDetails){
        String login = userDetails.getUsername();
        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()){
            Order order = orderService.placeOrder(user.get().getId());
            if(order != null){
                return ResponseEntity.ok(order);
            }else{
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("change={orderId}-{newStatus}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable String orderId, @PathVariable String newStatus){
        boolean operationStatus = orderService.changeOrderStatus(orderId, newStatus);
        if(operationStatus){
            return ResponseEntity.ok("Changed to " + newStatus);
        }
        return ResponseEntity.notFound().build();
    }
}
