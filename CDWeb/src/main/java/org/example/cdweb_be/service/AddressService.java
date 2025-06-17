package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.dto.request.AddressCreateRequest;
import org.example.cdweb_be.dto.request.AddressRequest;
import org.example.cdweb_be.dto.request.AddressUpdateRequest;
import org.example.cdweb_be.dto.response.AddressResponse;
import org.example.cdweb_be.entity.Address;
import org.example.cdweb_be.entity.District;
import org.example.cdweb_be.entity.Province;
import org.example.cdweb_be.entity.Ward;
import org.example.cdweb_be.enums.Role;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.AddressMapper;
import org.example.cdweb_be.respository.*;
import org.example.cdweb_be.utils.AddressUltils;
import org.example.cdweb_be.utils.responseUtilsAPI.DeliveryMethodUtil;
import org.springframework.security.access.prepost.PreAuthorize;
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
    MessageProvider messageProvider;
    RoleService roleService;
    int sendProvinceId =2;
    int sendDistrictId = 1231;
    @PreAuthorize("isAuthenticated()")
    public List<AddressResponse> getAll(String token) {
        long userId = authenticationService.getUserId(token);
        List<Address> addresses = addressRepository.findByUserId(userId);
        List<AddressResponse> addressResponses = addresses.stream().map(addressMapper::toAddressResponse).collect(Collectors.toList());
        return addressResponses;
    }
    @PreAuthorize("isAuthenticated()")
    public AddressResponse addAddress(String token, AddressRequest request) {
        long userId = authenticationService.getUserId(token);
        Address address = convertAddressRequestToAddres(request);
        Optional<Address> addressOptional = addressRepository.findByAllInfo(
                userId, request.getProvinceId(), request.getDistrictId(),
                request.getWardId(), request.getHouseNumber());
        if (addressOptional.isPresent()) {
            throw new AppException(messageProvider,ErrorCode.ADDRESS_EXISTED);
        }
        address.setUser(userRepository.findById(userId).get());
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }
    @PreAuthorize("isAuthenticated()")
    public AddressResponse updateAddress(String token, AddressUpdateRequest request){
        long userId = authenticationService.getUserId(token);
        Address address = addressRepository.findById(request.getAddressId()).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.ADDRESS_NOT_EXISTS)
        );
        if(userId != address.getUser().getId()) throw new AppException(messageProvider,ErrorCode.ADDRESS_UNAUTHORIZED);
        Optional<Address> addressOptional = addressRepository.findByAllInfo(
                userId, request.getProvinceId(), request.getDistrictId(),
                request.getWardId(), request.getHouseNumber());
        if (addressOptional.isPresent()) {
            throw new AppException(messageProvider,ErrorCode.ADDRESS_EXISTED);
        }
        Address addressUpdate = convertAddressRequestToAddres(request);
        address.setProvince(addressUpdate.getProvince());
        address.setDistrict(addressUpdate.getDistrict());
        address.setWard(addressUpdate.getWard());
        address.setHouseNumber(request.getHouseNumber());
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }
    @PreAuthorize("isAuthenticated()")
    public String deleteAddress(String token, long addressId){
        long userId = authenticationService.getUserId(token);
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.ADDRESS_NOT_EXISTS)
        );
        if(userId != address.getUser().getId()) throw new AppException(messageProvider,ErrorCode.ADDRESS_UNAUTHORIZED);
        addressRepository.deleteById(addressId);
        return messageProvider.getMessage("address.delete");
    }
    public Address convertAddressRequestToAddres(AddressRequest request){
        Province province = provinceRepository.findById(request.getProvinceId()).orElseThrow(

                () -> new AppException(messageProvider,ErrorCode.PROVINCE_NOT_EXISTS)
        );
        District district = districtRepository.findByIdAndProvinceId(request.getDistrictId(), province.getId()).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.DISTRICT_NOT_EXISTS)
        );
        if (district.getProvinceId() != province.getId()) {
            throw new AppException(messageProvider,ErrorCode.DISTRICT_INVALID);
        }

        Ward ward = wardRepository.findByIdAndDistrictId(request.getWardId(), district.getId()).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.WARD_NOT_EXISTS)
        );
        if (ward.getDistrictId() != district.getId()) {
            throw new AppException(messageProvider,ErrorCode.WARD_INVALID);
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
                new AppException(messageProvider,ErrorCode.ADDRESS_NOT_EXISTS));
        List<DeliveryMethodUtil> deliveryMethodUtils = AddressUltils.getInfoShips(sendProvinceId+"", sendDistrictId+"", address.getProvince().getId()+"", address.getDistrict().getId()+"");
        return deliveryMethodUtils;
    }
    public List<DeliveryMethodUtil> getInfoShip(AddressRequest request){
        List<DeliveryMethodUtil> deliveryMethodUtils = AddressUltils.getInfoShips(sendProvinceId+"", sendDistrictId+"", request.getProvinceId()+"", request.getDistrictId()+"");
        return deliveryMethodUtils;
    }
    public Address getAddress(long userId,AddressRequest request){
        Optional<Address> addressOptional = addressRepository.findByAllInfo(
                userId, request.getProvinceId(), request.getDistrictId(),
                request.getWardId(), request.getHouseNumber());
        if(addressOptional.isPresent()) return addressOptional.get();
        Address address = convertAddressRequestToAddres(request);
        address.setUser(userRepository.findById(userId).get());
        return (addressRepository.save(address));
    }

}
