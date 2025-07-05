package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Override
    public String createCheckoutSession(String orderId) {
        return null;
    }

    @Override
    public void handleWebhook(String payload, String signature) {

    }
}
