package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.LocationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationController {
    LocationService locationService;
    @GetMapping("/provinces")
    public ApiResponse getAllProvinces(){
        return new ApiResponse(locationService.getAllProvinces());
    }
    @GetMapping("/provinces/{provinceName}")
    public ApiResponse getProvincesByName(@PathVariable String provinceName){
        return new ApiResponse(locationService.getProvincesByName(provinceName));
    }
    @GetMapping("/districts")
    public ApiResponse getAllDistricts(){
        return new ApiResponse(locationService.getAllDistricts());
    }
    @GetMapping("/districts/{provinceId}")
    public ApiResponse getDistrictsByProvince(@PathVariable long provinceId){
        return new ApiResponse(locationService.getDistrictsByProvince(provinceId));
    }
    @GetMapping("/districts/{districtName}/province/{provinceId}")
    public ApiResponse getDistrictsByProvinceAndName(@PathVariable long provinceId, @PathVariable String districtName){
        return new ApiResponse(locationService.getDistrictsByProvinceAndName(provinceId, districtName));
    }
    @GetMapping("/wards")
    public ApiResponse getAllWards(){
        return new ApiResponse(locationService.getAllWards());
    }
    @GetMapping("/wards/{districtId}")
    public ApiResponse getWardsByDistrict(@PathVariable long districtId){
        return new ApiResponse(locationService.getWardsByDistrict(districtId));
    }
    @GetMapping("/wards/{wardName}/district/{districtId}")
    public ApiResponse getWardsByDistrictAndName(@PathVariable long districtId, @PathVariable String wardName){
        return new ApiResponse(locationService.getWardsByDistrictAndName(districtId, wardName));
    }
//    @PostMapping("/writes")
//    public ApiResponse write(){
//        return new ApiResponse(locationService.writes());
//    }
//    @GetMapping("/saveProvince")
//    public ApiResponse saveProvince(){
//        return new ApiResponse(locationService.saveAllProvince());
//    }
//    @GetMapping("/saveDistrict")
//    public ApiResponse saveDistrict(){
//        return new ApiResponse(locationService.saveAllDistrict());
//    }
//    @GetMapping("/saveWard")
//    public ApiResponse saveWard(){
//        return new ApiResponse(locationService.saveAllWard());
//    }
}
