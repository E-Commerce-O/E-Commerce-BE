package org.example.cdweb_be.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.entity.District;
import org.example.cdweb_be.entity.Province;
import org.example.cdweb_be.entity.Ward;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.AddressMapper;
import org.example.cdweb_be.respository.AddressRepository;
import org.example.cdweb_be.respository.DistrictRepository;
import org.example.cdweb_be.respository.ProvinceRepository;
import org.example.cdweb_be.respository.WardRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class LocationService {
    AddressRepository addressRepository;
    ProvinceRepository provinceRepository;
    DistrictRepository districtRepository;
    WardRepository wardRepository;
    AuthenticationService authenticationService;
    AddressMapper addressMapper;
    MessageProvider messageProvider;

    public List<Province> getAllProvinces(){
            return provinceRepository.findAll();
    }
    public List<Province> getProvincesByName(String provinceName){
        return provinceRepository.findByName(provinceName);
    }
    public List<District> getAllDistricts(){
        return districtRepository.findAll();
    }
    public List<District> getDistrictsByProvince(long provinceId){
        Province province = provinceRepository.findById(provinceId).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.PROVINCE_NOT_EXISTS)
        );
        return districtRepository.findByProvinceId(provinceId);
    }
    public List<District> getDistrictsByProvinceAndName(long provinceId, String districtName){
        Province province = provinceRepository.findById(provinceId).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.PROVINCE_NOT_EXISTS)
        );
        return districtRepository.findByProvinceIdAndName(provinceId, districtName);
    }
    public List<Ward> getAllWards(){
        return wardRepository.findAll();
    }
    public List<Ward> getWardsByDistrict(long districtId){
        District district = districtRepository.findById(districtId).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.DISTRICT_NOT_EXISTS)
        );
        return wardRepository.findByDistrictId(districtId);
    }
    public List<Ward> getWardsByDistrictAndName(long districtId, String wardName){
        District district = districtRepository.findById(districtId).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.DISTRICT_NOT_EXISTS)
        );
        return wardRepository.findByDistrictIdAndName(districtId, wardName);
    }
}
