package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.ProductDetailUpdateRequest;
import org.example.cdweb_be.dto.response.ProductDetailRespone;
import org.example.cdweb_be.entity.Product;
import org.example.cdweb_be.entity.ProductDetail;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.ProductMapper;
import org.example.cdweb_be.respository.ProductDetailRepository;
import org.example.cdweb_be.respository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class ProductDetailService {
    ProductRepository productRepository;
    ProductDetailRepository productDetailRepository;
    ProductMapper productMapper;
    public List<ProductDetailRespone> getDetailsByProduct(long productId){
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        List<ProductDetail> productDetails = productDetailRepository.findByProductId(productId);
        List<ProductDetailRespone> productDetailResponses = productDetails.
                stream().map(productDetail ->
                        productMapper.toProductDetailResponse(productDetail)).collect(Collectors.toList());
        return productDetailResponses;
    }
    public List<ProductDetailRespone> getDetailsByProductAndColor(long productId, long colorId){
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        List<ProductDetail> productDetails = productDetailRepository.findByProductIdAndProductColorId(productId, colorId);
        if(productDetails == null ||productDetails.size() ==0){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        List<ProductDetailRespone> productDetailResponses = productDetails.
                stream().map(productDetail -> productMapper.toProductDetailResponse(productDetail))
                .collect(Collectors.toList());
        return productDetailResponses;
    }
    public ProductDetailRespone getDetailsByProductAndColorAndSize(long productId, long colorId, long sizeId){
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        Optional<ProductDetail> productDetails = productDetailRepository
                .findByProductIdAndProductColorIdAndProductSizeId(productId, colorId, sizeId);
        if(productDetails.isEmpty()){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return productMapper.toProductDetailResponse(productDetails.get());
    }
    public List<ProductDetailRespone> getDetailsByProductAndSize(long productId, long sizeId){
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        List<ProductDetail> productDetails = productDetailRepository.findByProductIdAndProductSizeId(productId, sizeId);
        if(productDetails == null || productDetails.size() ==0){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        List<ProductDetailRespone> productDetailResponses = productDetails.
                stream().map(productDetail -> productMapper.toProductDetailResponse(productDetail)).collect(Collectors.toList());
        return productDetailResponses;
    }
    public ProductDetailRespone update(ProductDetailUpdateRequest request){
        ProductDetail productDetail = productDetailRepository.findById(request.getProductDetailId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_DETAIL_NOT_EXISTS));
        productDetail.setPrice(request.getPrice());
        productDetail.setDiscount(request.getDiscount());
        return productMapper.toProductDetailResponse(productDetailRepository.save(productDetail));
    }
}
