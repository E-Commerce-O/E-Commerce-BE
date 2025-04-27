package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.AddressRequest;
import org.example.cdweb_be.dto.request.AddressUpdateRequest;
import org.example.cdweb_be.dto.response.AddressResponse;
import org.example.cdweb_be.entity.Address;
import org.example.cdweb_be.entity.District;
import org.example.cdweb_be.entity.Province;
import org.example.cdweb_be.entity.Ward;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.AddressMapper;
import org.example.cdweb_be.respository.*;
import org.example.cdweb_be.utils.AddressUltils;
import org.example.cdweb_be.utils.responseUtilsAPI.DeliveryMethodUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AddressService {
    AuthenticationService authenticationService;
    AddressRepository addressRepository;
    AddressMapper addressMapper;
    ProvinceRepository provinceRepository;
    DistrictRepository districtRepository;
    WardRepository wardRepository;
    UserRepository userRepository;
    int sendProvinceId =2;
    int sendDistrictId = 1231;

    public List<AddressResponse> getAll(String token) {
        long userId = authenticationService.getUserId(token);
        List<Address> addresses = addressRepository.findByUserId(userId);
        List<AddressResponse> addressResponses = addresses.stream().map(addressMapper::toAddressResponse).collect(Collectors.toList());
        return addressResponses;
    }

    public AddressResponse addAddress(String token, AddressRequest request) {
        long userId = authenticationService.getUserId(token);
        Address address = convertAddressRequestToAddres(request);
        Optional<Address> addressOptional = addressRepository.findByAllInfo(
                userId, request.getProvinceId(), request.getDistrictId(),
                request.getWardId(), request.getHouseNumber());
        if (addressOptional.isPresent()) {
            throw new AppException(ErrorCode.ADDRESS_EXISTED);
        }
        address.setUser(userRepository.findById(userId).get());
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }
    public AddressResponse updateAddress(String token, AddressUpdateRequest request){
        long userId = authenticationService.getUserId(token);
        Address address = addressRepository.findById(request.getAddressId()).orElseThrow(
                () -> new AppException(ErrorCode.ADDRESS_NOT_EXISTS)
        );
        if(userId != address.getUser().getId()) throw new AppException(ErrorCode.ADDRESS_UNAUTHORIZED);
        Optional<Address> addressOptional = addressRepository.findByAllInfo(
                userId, request.getProvinceId(), request.getDistrictId(),
                request.getWardId(), request.getHouseNumber());
        if (addressOptional.isPresent()) {
            throw new AppException(ErrorCode.ADDRESS_EXISTED);
        }
        Address addressUpdate = convertAddressRequestToAddres(request);
        address.setProvince(addressUpdate.getProvince());
        address.setDistrict(addressUpdate.getDistrict());
        address.setWard(addressUpdate.getWard());
        address.setHouseNumber(request.getHouseNumber());
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }
    public String deleteAddress(String token, long addressId){
        long userId = authenticationService.getUserId(token);
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new AppException(ErrorCode.ADDRESS_NOT_EXISTS)
        );
        if(userId != address.getUser().getId()) throw new AppException(ErrorCode.ADDRESS_UNAUTHORIZED);
        addressRepository.deleteById(addressId);
        return "Address deleted successfully";
    }
    public Address convertAddressRequestToAddres(AddressRequest request){
        Province province = provinceRepository.findById(request.getProvinceId()).orElseThrow(

                () -> new AppException(ErrorCode.PROVINCE_NOT_EXISTS)
        );
        District district = districtRepository.findByIdAndProvinceId(request.getDistrictId(), province.getId()).orElseThrow(
                () -> new AppException(ErrorCode.DISTRICT_NOT_EXISTS)
        );
        if (district.getProvinceId() != province.getId()) {
            throw new AppException(ErrorCode.DISTRICT_INVALID);
        }

        Ward ward = wardRepository.findByIdAndDistrictId(request.getWardId(), district.getId()).orElseThrow(
                () -> new AppException(ErrorCode.WARD_NOT_EXISTS)
        );
        if (ward.getDistrictId() != district.getId()) {
            throw new AppException(ErrorCode.WARD_INVALID);
        }
        Address address = Address.builder()
                .province(province)
                .district(district)
                .ward(ward)
                .houseNumber(request.getHouseNumber())
                .build();
        return address;
    }
    public List<DeliveryMethodUtil> getInfoShip(long addressId){
        Address address = addressRepository.findById(addressId).orElseThrow(() ->
                new AppException(ErrorCode.ADDRESS_NOT_EXISTS));
        List<DeliveryMethodUtil> deliveryMethodUtils = AddressUltils.getInfoShips(sendProvinceId+"", sendDistrictId+"", address.getProvince().getId()+"", address.getDistrict().getId()+"");
        return deliveryMethodUtils;
    }

}
