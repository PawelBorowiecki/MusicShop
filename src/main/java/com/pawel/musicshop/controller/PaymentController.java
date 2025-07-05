package com.pawel.musicshop.controller;

import com.pawel.musicshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout/{orderId}")
    public ResponseEntity<String> createCheckoutSession(@PathVariable String orderId){
        String url = paymentService.createCheckoutSession(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature){
        paymentService.handleWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/success")
    public ResponseEntity<String> success(){
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel(){
        return ResponseEntity.ok("Cancel");
    }
}
