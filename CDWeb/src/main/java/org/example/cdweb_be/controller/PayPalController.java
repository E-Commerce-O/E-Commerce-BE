package org.example.cdweb_be.controller;

import org.example.cdweb_be.dto.response.OrderResponse;
import org.example.cdweb_be.service.OrderService;
import org.example.cdweb_be.service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PayPalController {
    @Autowired
    private PayPalService payPalService;
    @Autowired
    private OrderService orderService;
    @PostMapping("/api/paypal/create-order")
    public ResponseEntity<?> createOrder(@RequestParam long orderId) {
        OrderResponse order =orderService.getById(orderId);
        String accessToken = payPalService.getAccessToken();
        String orderIdPaypal = payPalService.createOrder(accessToken, order.getTotalPrice());
        return ResponseEntity.ok(Map.of("orderId", orderIdPaypal));
    }
    @PostMapping("/api/paypal/capture")
    public ResponseEntity<?> captureOrder(@RequestBody Map<String, String> body) {
        String orderIdPayPal = body.get("orderId");
        String accessToken = payPalService.getAccessToken();
        String captureResult = payPalService.captureOrder(accessToken, orderIdPayPal);
        if (captureResult.equals("COMPLETED")) {// thành công thanh toán

        }else {// thánh toán thất bại

        }
        return ResponseEntity.ok(Map.of("result", captureResult));
    }
}