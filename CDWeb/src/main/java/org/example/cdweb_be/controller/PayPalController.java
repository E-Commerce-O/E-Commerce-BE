package org.example.cdweb_be.controller;

import org.example.cdweb_be.dto.response.OrderResponse;
import org.example.cdweb_be.service.OrderService;
import org.example.cdweb_be.service.PayPalService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PayPalController {
    @Autowired
    private PayPalService payPalService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/api/paypal/create-order")
    public ResponseEntity<?> createOrder(@RequestParam long orderId) {
        OrderResponse order = orderService.getById(orderId);
        String accessToken = payPalService.getAccessToken();
        String orderIdPaypal = payPalService.createOrder(accessToken, order.getTotalPrice(), orderId);
        return ResponseEntity.ok(Map.of("orderId", orderIdPaypal));
    }

    @PostMapping("/api/paypal/capture")
    public ResponseEntity<?> captureOrder(@RequestBody Map<String, String> body) {
        String orderIdPayPal = body.get("orderId");
        String accessToken = payPalService.getAccessToken();
        String captureResult = payPalService.captureOrder(accessToken, orderIdPayPal);
        JSONObject jsonObject = new JSONObject(captureResult);
        JSONArray jsonArray = jsonObject.getJSONArray("purchase_units");
        if (jsonArray.length() == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "No purchase units found"));
        }else {
            JSONObject purchaseUnit = jsonArray.getJSONObject(0);
            JSONObject payments = purchaseUnit.getJSONObject("payments");
            String orderId = payments.getJSONArray("captures").getJSONObject(0).get("invoice_id").toString();
            try {
                Long orderIdLong = Long.parseLong(orderId);
                // update order status
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(Map.of("result", jsonObject.get("status").toString()));
    }
}