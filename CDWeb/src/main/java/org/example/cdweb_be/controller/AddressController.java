package org.example.cdweb_be.controller;

import io.swagger.annotations.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.request.AddressCreateRequest;
import org.example.cdweb_be.dto.request.AddressRequest;
import org.example.cdweb_be.dto.request.AddressUpdateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.AddressService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressService;
    @GetMapping("/getMyAddesses")
    public ApiResponse getMyAddresses(@RequestHeader("Authorization") String token){
        return new ApiResponse(addressService.getAll(token));
    }
    @GetMapping("/getInfoShips/{addressId}")
    public ApiResponse getInfoShips(@PathVariable long addressId){
        return new ApiResponse(addressService.getInfoShip(addressId));
    }
    @PostMapping("/add")
    public ApiResponse addAddress(@RequestHeader("Authorization") String token,@RequestBody AddressRequest request){
        return new ApiResponse(addressService.addAddress(token, request));
    }
    @PutMapping("/update")
    public ApiResponse addAddress(@RequestHeader("Authorization") String token,@RequestBody AddressUpdateRequest request){
        return new ApiResponse(addressService.updateAddress(token, request));
    }
    @DeleteMapping("/delete/{addressId}")
    public ApiResponse deleteAddress(@RequestHeader("Authorization") String token, @PathVariable("addressId") long addressId){
        return new ApiResponse(addressService.deleteAddress(token, addressId));
    }

}
