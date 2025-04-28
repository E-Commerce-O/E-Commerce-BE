package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.ProductDetailUpdateRequest;
import org.example.cdweb_be.dto.response.ProductDetailRespone;
import org.example.cdweb_be.entity.*;
import org.example.cdweb_be.enums.OrderStatus;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.ProductMapper;
import org.example.cdweb_be.respository.*;
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
    OrderItemRepository orderItemRepository;
    ProductImportRepository productImportRepository;

    public void initDetail(Product product, List<ProductColor> colors, List<ProductSize> sizes) {
        if (colors.isEmpty()) colors.add(new ProductColor());
        if (sizes.isEmpty()) sizes.add(new ProductSize());
        for (ProductColor color : colors) {
            for (ProductSize size : sizes) {
                Optional<ProductDetail> productDetailOptional =
                        productDetailRepository.findByProductIdAndColorIdAndSizeId(product.getId(), color.getId(), size.getId());
                if (productDetailOptional.isEmpty()) {
                    ProductDetail productDetail = ProductDetail.builder()
                            .product(product)
                            .price(product.getDefaultPrice())
                            .discount(product.getDefaultDiscount())
                            .build();
                    if (size.getId() > 0) productDetail.setSize(size);
                    if (color.getId() > 0) productDetail.setColor(color);
                    productDetailRepository.save(productDetail);

                }
            }
        }
    }

    public List<ProductDetailRespone> getDetailsByProduct(long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        List<ProductDetail> productDetails = productDetailRepository.findByProductId(productId);
        List<ProductDetailRespone> productDetailResponses = productDetails.stream().map(productDetail -> convertToProductDetailRespons(productDetail)).collect(Collectors.toList());

        return productDetailResponses;
    }

    public List<ProductDetailRespone> getDetailsByProductAndColor(long productId, long colorId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        List<ProductDetail> productDetails = productDetailRepository.findByProductIdAndColorId(productId, colorId);
        if (productDetails == null || productDetails.size() == 0) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        List<ProductDetailRespone> productDetailResponses = productDetails.stream().map(productDetail -> convertToProductDetailRespons(productDetail)).collect(Collectors.toList());
        return productDetailResponses;
    }

    public ProductDetailRespone getDetailsByProductAndColorAndSize(long productId, long colorId, long sizeId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        Optional<ProductDetail> productDetails = productDetailRepository.findByProductIdAndColorIdAndSizeId(productId, colorId, sizeId);
        if (productDetails.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return convertToProductDetailRespons(productDetails.get());
    }

    public List<ProductDetailRespone> getDetailsByProductAndSize(long productId, long sizeId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        List<ProductDetail> productDetails = productDetailRepository.findByProductIdAndSizeId(productId, sizeId);
        if (productDetails == null || productDetails.size() == 0) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        List<ProductDetailRespone> productDetailResponses = productDetails.stream().map(productDetail -> convertToProductDetailRespons(productDetail)).collect(Collectors.toList());
        return productDetailResponses;
    }

    public ProductDetailRespone update(ProductDetailUpdateRequest request) {
        ProductDetail productDetail = productDetailRepository.findById(request.getProductDetailId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_DETAIL_NOT_EXISTS));
        productDetail.setPrice(request.getPrice());
        productDetail.setDiscount(request.getDiscount());
        return convertToProductDetailRespons(productDetailRepository.save(productDetail));
    }
    public int getRemainingQuantity(ProductDetail productDetail){
        if(productDetail.getSize() ==null && productDetail.getColor() ==null){
            return getRemainingQuantity(productDetail.getProduct().getId());
        }else{
            if(productDetail.getColor() != null && productDetail.getSize() == null){
                return getRemainingQuantityWithoutSize(
                        productDetail.getProduct().getId(), productDetail.getColor().getId()
                );
            }else if( productDetail.getColor() == null && productDetail.getSize() != null){
                return getRemainingQuantityWithoutColor(
                        productDetail.getProduct().getId(), productDetail.getSize().getId()
                );
            }else{
                return getRemainingQuantityByAllInfo(
                        productDetail.getProduct().getId(), productDetail.getSize().getId(), productDetail.getColor().getId()
                );
            }
        }
    }
    public ProductDetailRespone convertToProductDetailRespons(ProductDetail productDetail){
        ProductDetailRespone result = productMapper.toProductDetailResponse(productDetail);
        result.setColor(productMapper.toProductColorResponse(productDetail.getColor()));
        result.setSize(productMapper.toProductSizeResponse(productDetail.getSize()));
        result.setQuantity(getRemainingQuantity(productDetail));
        return result;
    }
    public int getRemainingQuantityByAllInfo(long productId, long sizeId, long colorId){
        List<OrderItem> orderItems = orderItemRepository.findByProductAndColorAndSizeAndExceptStatus(
                productId, colorId, sizeId, OrderStatus.ST_DA_HUY);
        List<ProductImport> productImports = productImportRepository.findByProductAndColorAndSize(
                productId, colorId, sizeId);
//        log.info(productImports.size()+"");
        for(ProductImport productImport: productImports){
//            log.info(productImport.toString());
        }
        int totalImport = productImports.stream().mapToInt(
                ProductImport::getQuantity
        ).sum();
        int totalSale = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        return (totalImport - totalSale >0)?totalImport - totalSale:0;
    }
    public int getRemainingQuantity(long productId){
        List<OrderItem> orderItems = orderItemRepository.findByProductIdAndExceptStatus(productId, OrderStatus.ST_DA_HUY);
        List<ProductImport> productImports = productImportRepository.findByProductId(
                productId);
        int totalImport = productImports.stream().mapToInt(
                ProductImport::getQuantity
        ).sum();
        int totalSale = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        return (totalImport - totalSale >0)?totalImport - totalSale:0;
    }
    public int getRemainingQuantityWithoutColor(long productId, long sizeId){
        List<OrderItem> orderItems = orderItemRepository.findByProductAndSizeAndExceptStatus(
                productId, sizeId, OrderStatus.ST_DA_HUY);
        List<ProductImport> productImports = productImportRepository.findByProductAndSize(
                productId, sizeId);
        int totalImport = productImports.stream().mapToInt(
                ProductImport::getQuantity
        ).sum();
        int totalSale = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        return (totalImport - totalSale >0)?totalImport - totalSale:0;
    }
    public int getRemainingQuantityWithoutSize(long productId, long colorId){
        List<OrderItem> orderItems = orderItemRepository.findByProductAndColorAndExceptStatus(
                productId, colorId, OrderStatus.ST_DA_HUY);
        List<ProductImport> productImports = productImportRepository.findByProductAndColor(
                productId, colorId);
        int totalImport = productImports.stream().mapToInt(
                ProductImport::getQuantity
        ).sum();
        int totalSale = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        return (totalImport - totalSale >0)?totalImport - totalSale:0;
    }
    public double getPrice(Product product, ProductSize size, ProductColor color){
        if(size != null && color != null){
            return getDetailsByProductAndColorAndSize(product.getId(), color.getId(), size.getId()).getPrice();
        }else{
            if(size != null){
                return getDetailsByProductAndSize(product.getId(), size.getId()).get(0).getPrice();
            }else{
                return getDetailsByProductAndColor(product.getId(), color.getId()).get(0).getPrice();
            }
        }
    }
    public int getDiscount(Product product, ProductSize size, ProductColor color){
        if(size != null && color != null){
            return getDetailsByProductAndColorAndSize(product.getId(), color.getId(), size.getId()).getDiscount();
        }else{
            if(size != null){
                return getDetailsByProductAndSize(product.getId(), size.getId()).get(0).getDiscount();
            }else{
                return getDetailsByProductAndColor(product.getId(), color.getId()).get(0).getDiscount();
            }
        }
    }
    public int getRemainingQuantity(Product product, ProductSize size, ProductColor color){
        if(size != null && color != null){
            return getRemainingQuantityByAllInfo(product.getId(), size.getId(), color.getId());
        }else{
            if(size != null){
                return getRemainingQuantityWithoutColor(product.getId(), size.getId());
            }else{
                return getRemainingQuantityWithoutSize(product.getId(), color.getId());
            }
        }
    }
}
