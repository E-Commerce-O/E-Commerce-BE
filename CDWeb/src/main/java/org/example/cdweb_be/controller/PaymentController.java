package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.dto.response.OrderResponse;
import org.example.cdweb_be.enums.OrderStatus;
import org.example.cdweb_be.service.OrderService;
import org.example.cdweb_be.service.PaymentService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentController {
    PaymentService paymentService;
    OrderService orderService;
    @GetMapping()
    public ApiResponse getAllActive(){
        return new ApiResponse(paymentService.getAllActive());
    }
    @PostMapping("/paypal/create-order")
    public ResponseEntity<?> createOrder(@RequestParam long orderId) {
        OrderResponse order = orderService.getById(orderId);
        String accessToken = paymentService.getAccessToken();
        String orderIdPaypal = paymentService.createOrder(accessToken, order.getTotalPrice(), orderId);
        return ResponseEntity.ok(Map.of("orderId", orderIdPaypal));
    }

    @PostMapping("/paypal/capture")
    public ResponseEntity<?> captureOrder(@RequestBody Map<String, String> body) {
        String orderIdPayPal = body.get("orderId");
        String accessToken = paymentService.getAccessToken();
        String captureResult = paymentService.captureOrder(accessToken, orderIdPayPal);
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

                // update order statusor
                orderService.updateStatus(orderIdLong, OrderStatus.ST_DANG_CBI_HANG);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(Map.of("result", jsonObject.get("status").toString()));
    }
}