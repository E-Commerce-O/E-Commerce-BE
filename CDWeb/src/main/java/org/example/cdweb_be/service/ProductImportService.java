package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.dto.request.ProductImportCreateRequest;
import org.example.cdweb_be.dto.request.ProductImportUpdateRequest;
import org.example.cdweb_be.dto.response.PagingResponse;
import org.example.cdweb_be.dto.response.ProductImportResponse;
import org.example.cdweb_be.dto.response.UserImportProduct;
import org.example.cdweb_be.entity.*;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.respository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImportService {
    MessageProvider messageProvider;
    ProductImportRepository productImportRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    ProductColorRepository productColorRepository;
    ProductSizeRepository productSizeRepository;
    AuthenticationService authenticationService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PagingResponse getAllProductImports(int page, int size) {
        List<ProductImportResponse> productImportResponses = productImportRepository.findAll(PageRequest.of(page-1, size))
                .stream().map(productImport -> convertToProductImportResponse(productImport)).collect(Collectors.toList());
        return PagingResponse.<ProductImportResponse>builder()
                .page(page)
                .size(size)
                .totalItem(productImportRepository.count())
                .data(productImportResponses)
                .build();
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PagingResponse getProductImportByProduct(long productId, int page, int size) {
        List<ProductImportResponse> productImportResponses = productImportRepository.findByProductId(productId, PageRequest.of(page-1, size))
                .stream().map(productImport -> convertToProductImportResponse(productImport)).collect(Collectors.toList());
        return PagingResponse.<ProductImportResponse>builder()
                .page(page)
                .size(size)
                .totalItem(productImportRepository.count())
                .data(productImportResponses)
                .build();
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ProductImportResponse add(String token, ProductImportCreateRequest request){
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.PRODUCT_NOT_EXISTS)
        );
        ProductColor productColor = null;
        if(!productColorRepository.findByProductId(product.getId()).isEmpty()){
            productColor = productColorRepository.findById(request.getColorId()).orElseThrow(() ->
                    new AppException(messageProvider,ErrorCode.PRODUCT_COLOR_NOT_EXISTS));
            if(productColorRepository.findByIdAndProductId(productColor.getId(), product.getId()).isEmpty()) throw new AppException(messageProvider,ErrorCode.PRODUCT_COLOR_INVALID);
        }
        ProductSize size = null;
        if(!productSizeRepository.findByProductId(product.getId()).isEmpty()){
            size = productSizeRepository.findById(request.getSizeId()).orElseThrow(() ->
                    new AppException(messageProvider,ErrorCode.PRODUCT_SIZE_NOT_EXISTS));
            if(size.getProduct().getId() != product.getId()) throw new AppException(messageProvider,ErrorCode.PRODUCT_SIZE_INVALID);
        }
        if(request.getQuantity() <=0) throw new AppException(messageProvider,ErrorCode.QUANTITY_INVALID);
        if(request.getPrice() <=0) throw new AppException(messageProvider,ErrorCode.PRICE_INVALID);
        long userId = authenticationService.getUserId(token);
        User user = userRepository.findById(request.getUserImported()).orElse(
                userRepository.findById(userId).get()
        );
        ProductImport newProductImport = ProductImport.builder()
                .user(user)
                .product(product)
                .color(productColor)
                .size(size)
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .importedAt(request.getImportedAt() != null?request.getImportedAt():new Date(System.currentTimeMillis()))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        return convertToProductImportResponse(productImportRepository.save(newProductImport));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ProductImportResponse update(String token, ProductImportUpdateRequest request){
        ProductImport productImport = productImportRepository.findById(request.getImportId()).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.PRODUCT_IMPORT_NOT_EXISTS));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new AppException(messageProvider,ErrorCode.PRODUCT_NOT_EXISTS)
        );
        ProductColor productColor = productColorRepository.findById(request.getColorId()).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.PRODUCT_COLOR_NOT_EXISTS));
        if(productColorRepository.findByIdAndProductId(productColor.getId(), product.getId()).isEmpty()) throw new AppException(messageProvider,ErrorCode.PRODUCT_COLOR_INVALID);
        ProductSize size = productSizeRepository.findById(request.getSizeId()).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.PRODUCT_SIZE_NOT_EXISTS));
        if(size.getProduct().getId() != product.getId()) throw new AppException(messageProvider,ErrorCode.PRODUCT_SIZE_INVALID);
        if(request.getQuantity() <=0) throw new AppException(messageProvider,ErrorCode.QUANTITY_INVALID);
        if(request.getPrice() <=0) throw new AppException(messageProvider,ErrorCode.PRICE_INVALID);
        long userId = authenticationService.getUserId(token);
        User user = userRepository.findById(request.getUserImported()).orElse(
                userRepository.findById(userId).get()
        );
        boolean change = false;
        if (request.getProductId()!=productImport.getProduct().getId()) {
            productImport.setProduct(product);
            change = true;
        }
        if (request.getColorId()!= productImport.getColor().getId()) {
            productImport.setColor(productColor);
            change = true;
        }
        if (request.getSizeId()!= productImport.getSize().getId()) {
            productImport.setSize(size);
            change = true;
        }
        if (user.getId()!= productImport.getUser().getId()) {
            productImport.setUser(user);
            change = true;
        }
        if (request.getPrice()!= productImport.getPrice()) {
            productImport.setPrice(request.getPrice());
            change = true;
        }
        if (request.getQuantity()!= productImport.getQuantity()) {
            productImport.setQuantity(request.getQuantity());
            change = true;
        }
        if (request.getImportedAt() != null && productImport.getImportedAt() != null) {
            if (request.getImportedAt().getDay() != productImport.getImportedAt().getDay() ||
                    request.getImportedAt().getMonth() != productImport.getImportedAt().getMonth() ||
                    request.getImportedAt().getYear() != productImport.getImportedAt().getYear()) {
                productImport.setImportedAt(request.getImportedAt());
                change = true;
            }
        }
        if (!change) {
            throw new AppException(messageProvider,ErrorCode.CANT_UPDATE_IMPORT);
        }
        return convertToProductImportResponse(productImportRepository.save(productImport));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public String delete(long importId){
        ProductImport productImport = productImportRepository.findById(importId).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.PRODUCT_IMPORT_NOT_EXISTS));
        productImportRepository.delete(productImport);
        return messageProvider.getMessage("import.delete");
    }
    public ProductImportResponse convertToProductImportResponse(ProductImport productImport){
        return ProductImportResponse.builder()
                .id(productImport.getId())
                .userImported(UserImportProduct.builder()
                        .userId(productImport.getUser().getId())
                        .fullName(productImport.getUser().getFullName())
                        .role(productImport.getUser().getRole())
                        .build())
                .productId(productImport.getProduct().getId())
                .productName(productImport.getProduct().getName())
                .color(productImport.getColor())
                .productSize(productImport.getSize())
                .priceImport(productImport.getPrice())
                .quantityImport(productImport.getQuantity())
                .importedAt(productImport.getImportedAt())
                .build();
    }
}
