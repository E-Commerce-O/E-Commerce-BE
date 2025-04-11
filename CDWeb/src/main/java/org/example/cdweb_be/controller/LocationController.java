package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.LocationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationController {
    LocationService locationService;
    @GetMapping("/getAllProvinces")
    public ApiResponse getAllProvinces(){
        return new ApiResponse(locationService.getAllProvinces());
    }
    @GetMapping("/getProvincesByName/{provinceName}")
    public ApiResponse getProvincesByName(@PathVariable String provinceName){
        return new ApiResponse(locationService.getProvincesByName(provinceName));
    }
    @GetMapping("/getAllDistricts")
    public ApiResponse getAllDistricts(){
        return new ApiResponse(locationService.getAllDistricts());
    }
    @GetMapping("/getDistrictsByProvince/{provinceId}")
    public ApiResponse getDistrictsByProvince(@PathVariable long provinceId){
        return new ApiResponse(locationService.getDistrictsByProvince(provinceId));
    }
    @GetMapping("/getDistrictsByProvince")
    public ApiResponse getDistrictsByProvinceAndName(@RequestParam long provinceId, @RequestParam String districtName){
        return new ApiResponse(locationService.getDistrictsByProvinceAndName(provinceId, districtName));
    }
    @GetMapping("/getAllWards")
    public ApiResponse getAllWards(){
        return new ApiResponse(locationService.getAllWards());
    }
    @GetMapping("/getWardsByDistrict/{districtId}")
    public ApiResponse getWardsByDistrict(@PathVariable long districtId){
        return new ApiResponse(locationService.getWardsByDistrict(districtId));
    }
    @GetMapping("/getWardsByDistrictAndName")
    public ApiResponse getWardsByDistrictAndName(@RequestParam long districtId, @RequestParam String wardName){
        return new ApiResponse(locationService.getWardsByDistrictAndName(districtId, wardName));
    }
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
