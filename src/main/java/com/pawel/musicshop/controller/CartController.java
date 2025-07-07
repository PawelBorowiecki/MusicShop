package com.pawel.musicshop.controller;

import com.pawel.musicshop.model.Cart;
import com.pawel.musicshop.model.User;
import com.pawel.musicshop.service.CartService;
import com.pawel.musicshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<Cart> getAllCarts(){
        return cartService.findAll();
    }

    @GetMapping("/get={id}")
    public ResponseEntity<Cart> getCartById(@PathVariable String id){
        return cartService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user")
    public ResponseEntity<Cart> getUserCart(@AuthenticationPrincipal UserDetails userDetails){
        String login = userDetails.getUsername();
        Optional<User> user = userService.findByLogin(login);
        return user.map(value -> ResponseEntity.ok(value.getCart())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/price")
    public ResponseEntity<Double> getCartTotalPrice(@AuthenticationPrincipal UserDetails userDetails){
        String login = userDetails.getUsername();
        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()){
            double result = cartService.getProductsInCartTotalPrice(user.get().getCart().getId());
            if(result >= 0){
                return ResponseEntity.ok(result);
            }
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/add={cdId}-{quantity}")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String cdId, @PathVariable int quantity){
        String login = userDetails.getUsername();
        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()){
            boolean status = cartService.addMusicCDToCart(cdId, user.get().getId(), quantity);
            if(status){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{cdId}")
    public ResponseEntity<?> deleteFromCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String cdId){
        String login = userDetails.getUsername();
        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()){
            boolean status = cartService.deleteMusicCDFromCart(cdId, user.get().getId());
            if(status){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
