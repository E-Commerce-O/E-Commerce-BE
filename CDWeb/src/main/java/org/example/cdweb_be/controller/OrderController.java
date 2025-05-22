package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.request.OrderCreateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;
//    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ApiResponse getAll(){
        return new ApiResponse(orderService.getAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/status/{status}")
    public ApiResponse getByType(@PathVariable int status){
        return new ApiResponse(orderService.getAllByStatus(status));
    }
    @GetMapping("/myOrders")
    public ApiResponse getMyOrders(@RequestHeader("Authorization") String token){
        return new ApiResponse(orderService.getMyOrders(token));
    }
    @GetMapping("/{orderId}")
    public ApiResponse getById(@PathVariable long orderId){
        return new ApiResponse(orderService.getById(orderId));
    }

    @PostMapping
    public ApiResponse add(@RequestHeader("Authorization") String token, @RequestBody OrderCreateRequest request){
        return new ApiResponse(orderService.add(token, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/status")
    public ApiResponse updateStatus(@RequestParam long orderId, @RequestParam int status){
        return new ApiResponse(orderService.updateStatus(orderId, status));
    }
    @PutMapping("/cancel/{orderId}")
    public ApiResponse cancelOrder(@RequestHeader("Authorization") String token, @PathVariable long orderId){
        return new ApiResponse(orderService.cancelOrder(token, orderId));
    }
    @PutMapping("/return/{orderId}")
    public ApiResponse returnOrder(@RequestHeader("Authorization") String token, @PathVariable long orderId){
        return new ApiResponse(orderService.returnOrder(token, orderId));
    }
}
