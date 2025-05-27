package org.example.cdweb_be.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;
@Service
public class PayPalService {
    public String createOrder(String accessToken,double price) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> amount = Map.of(
                "currency_code", "USD",
                "value", String.format("%.2f", price)
        );

        Map<String, Object> purchaseUnit = Map.of(
                "amount", amount
        );

        Map<String, Object> applicationContext = Map.of(
                "shipping_preference", "NO_SHIPPING"
//                ,"return_url", returnUrl,
//                "cancel_url", cancelUrl
        );

        Map<String, Object> body = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(purchaseUnit),
                "application_context", applicationContext
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> res = restTemplate.postForEntity(
                "https://api-m.sandbox.paypal.com/v2/checkout/orders", entity, Map.class);

        return (String) res.getBody().get("id");
    }

    public String getAccessToken() {
        String clientId = "AZ7qEYUjE86IfhANwvs_3Vzfqgi4EYPZ2dehMb-T-nAHJr-kKStnyPAYTPJYWkoObyjLO2j7oEm4ppjZ";
        String secret = "EM_KuUC_H3aMDhJZqbPLLdfz9Bmom7gY3MXwm0uo2k1e5KT2w497AzGAkqYG2HVYC8ipDbXgl4w98tv6";
        String auth = Base64.getEncoder().encodeToString((clientId + ":" + secret).getBytes());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + auth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(map, headers);

        ResponseEntity<Map> res = restTemplate.postForEntity(
                "https://api-m.sandbox.paypal.com/v1/oauth2/token", req, Map.class);
        return (String) res.getBody().get("access_token");
    }
    public String captureOrder(String accessToken, String orderIdPayPal) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Map> res = restTemplate.postForEntity(
                "https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderIdPayPal + "/capture",
                entity,
                Map.class
        );
        return res.getBody().get("status").toString();
    }
}
