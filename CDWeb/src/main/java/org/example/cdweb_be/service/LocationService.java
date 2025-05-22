package org.example.cdweb_be.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
                () -> new AppException(ErrorCode.PROVINCE_NOT_EXISTS)
        );
        return districtRepository.findByProvinceId(provinceId);
    }
//    public List<String> getDistrictsByProvince(long provinceId){
//        Province province = provinceRepository.findById(provinceId).orElseThrow(
//                () -> new AppException(ErrorCode.PROVINCE_NOT_EXISTS)
//        );
//        return districtRepository.findByProvinceId(provinceId).stream().map(district ->
//                district.getName()).collect(Collectors.toList());
//    }
    public List<District> getDistrictsByProvinceAndName(long provinceId, String districtName){
        Province province = provinceRepository.findById(provinceId).orElseThrow(
                () -> new AppException(ErrorCode.PROVINCE_NOT_EXISTS)
        );
        return districtRepository.findByProvinceIdAndName(provinceId, districtName);
    }
    public List<Ward> getAllWards(){
        return wardRepository.findAll();
    }
    public List<Ward> getWardsByDistrict(long districtId){
        District district = districtRepository.findById(districtId).orElseThrow(
                () -> new AppException(ErrorCode.DISTRICT_NOT_EXISTS)
        );
        return wardRepository.findByDistrictId(districtId);
    }
//public List<String> getWardsByDistrict(long districtId){
//    District district = districtRepository.findById(districtId).orElseThrow(
//            () -> new AppException(ErrorCode.DISTRICT_NOT_EXISTS)
//    );
//    return wardRepository.findByDistrictId(districtId).stream().map(ward ->
//            ward.getName()).collect(Collectors.toList());
//}
    public List<Ward> getWardsByDistrictAndName(long districtId, String wardName){
        District district = districtRepository.findById(districtId).orElseThrow(
                () -> new AppException(ErrorCode.DISTRICT_NOT_EXISTS)
        );
        return wardRepository.findByDistrictIdAndName(districtId, wardName);
    }
    public String writes(){
        long[] provinceIds = {1, 2, 3 ,4};
        List<Ward> wards = new ArrayList<>();
        List<District> districtList = new ArrayList<>( );
        for(long id: provinceIds){
            List<District> districts = districtRepository.findByProvinceId(id);
            districtList.addAll(districts);
            for(District district: districts){
                wards.addAll(wardRepository.findByDistrictId(district.getId()));
            }
        }
        String filePath = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("D:\\Program\\Spring-Boot\\_21130356_NgoQuangHieu_Bai1\\src\\main\\java\\org\\example\\_21130356_ngoquanghieu_bai1\\fakeLocation\\wards.json"), wards);
            objectMapper.writeValue(new File("D:\\Program\\Spring-Boot\\_21130356_NgoQuangHieu_Bai1\\src\\main\\java\\org\\example\\_21130356_ngoquanghieu_bai1\\fakeLocation\\districts.json"), districtList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "Writes success";
    }
//    public List<Province> saveAllProvince(){
//        try{
//            List<ProvinceUtil> provinceUtils = AddressUltils.getProvinces("-1");
//            List<Province> provinces = provinceUtils.stream()
//                    .map(provinceUtil -> new Province(provinceUtil)).collect(Collectors.toList());
//            return provinceRepository.saveAll(provinces);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public List<District> saveAllDistrict(){
//        try {
//            List<Province> provinces = provinceRepository.findAll();
//            for(Province province: provinces){
//                List<DistrictUtil> districtUtils = AddressUltils.getDistricts(province.getId()+"");
//                List<District> districts = districtUtils.stream()
//                        .map(districtUtil -> new District(districtUtil)).collect(Collectors.toList());
//                districtRepository.saveAll(districts);
//            }
//            return districtRepository.findAll();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public List<Ward> saveAllWard(){
//        try {
//
//            List<District> districts = districtRepository.findAll();
//            for(District district: districts){
//                List<WardUtil> wardUtils = AddressUltils.getWards(district.getId()+"");
//                List<Ward> wards = wardUtils.stream().map(
//                        wardUtil -> new Ward(wardUtil)
//                ).collect(Collectors.toList());
//                wardRepository.saveAll(wards);
//            }
//            return wardRepository.findAll();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
