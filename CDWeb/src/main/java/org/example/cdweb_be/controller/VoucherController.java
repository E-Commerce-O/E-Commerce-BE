package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.request.ApplyVoucherRequest;
import org.example.cdweb_be.dto.request.VoucherRequest;
import org.example.cdweb_be.dto.request.VoucherUpdateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.VoucherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {
    VoucherService voucherService;
    @GetMapping("/genVoucherCode")
    public ApiResponse genVoucherCode(){
        return new ApiResponse(voucherService.genCode());
    }
    @GetMapping("/getAll")
    public ApiResponse getAll(){
        return new ApiResponse(voucherService.getAll());
    }
    @GetMapping("/getByCode/{voucherCode}")
    public ApiResponse getByCode(@PathVariable String voucherCode){
        return new ApiResponse(voucherService.getByCode(voucherCode));
    }
    @GetMapping("/getByType/{type}")
    public ApiResponse getByType(@PathVariable int type){
        return new ApiResponse(voucherService.getByType(type));
    }

    @PostMapping("/add")
    public ApiResponse add(@RequestBody VoucherRequest request){
        return new ApiResponse(voucherService.add(request));
    }
    @PostMapping("/applyVoucher")
    public ApiResponse applyVoucher(@RequestBody ApplyVoucherRequest request){
        return new ApiResponse(voucherService.applyVouhcer(request));
    }
    @PutMapping("/update/{id}")
    public ApiResponse update(@PathVariable long id, @RequestBody VoucherRequest request){
        return new ApiResponse(voucherService.update(id, request));
    }
}
