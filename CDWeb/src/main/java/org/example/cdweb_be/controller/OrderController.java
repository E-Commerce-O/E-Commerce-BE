package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.request.OrderCreateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;
    @GetMapping("/getAll")
    public ApiResponse getAll(){
        return new ApiResponse(orderService.getAll());
    }
    @GetMapping("/getByStatus/{status}")
    public ApiResponse getByType(@PathVariable int status){
        return new ApiResponse(orderService.getAllByStatus(status));
    }
    @GetMapping("/getMyOrders")
    public ApiResponse getMyOrders(@RequestHeader("Authorization") String token){
        return new ApiResponse(orderService.getMyOrders(token));
    }
    @PostMapping("/add")
    public ApiResponse add(@RequestHeader("Authorization") String token, @RequestBody OrderCreateRequest request){
        return new ApiResponse(orderService.add(token, request));
    }

}
