package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.AddProductImageRequest;
import org.example.cdweb_be.dto.request.ProductCreateRequest;
import org.example.cdweb_be.dto.request.ProductUpdateRequest;
import org.example.cdweb_be.dto.response.ProductResponse;
import org.example.cdweb_be.entity.*;
import org.example.cdweb_be.enums.OrderStatus;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.ProductMapper;
import org.example.cdweb_be.mapper.ProductSizeMapper;
import org.example.cdweb_be.respository.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class ProductService {
    TagRepository tagRepository;
    ProductRepository productRepository;
    ProductSizeRepository productSizeRepository;
    ProductColorRepository productColorRepository;
    ProductMapper productMapper;
    ProductSizeMapper productSizeMapper;
    ProductImageRepository productImageRepository;
    CategoryRepository categoryRepository;
    ProductDetailRepository productDetailRepository;
    ProductTagRepositoty productTagRepositoty;
    OrderItemRepository orderItemRepository;
    ProductImportRepository productImportRepository;
    ProductReviewRepository productReviewRepository;
    public Product addProduct(ProductCreateRequest request){
        Product product = productMapper.toProduct(request);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Optional<Category> categoryOptional = categoryRepository.findById(request.getCategoryId());
        if(categoryOptional.isEmpty())
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTS);
        product.setCategory(categoryOptional.get());
        product= productRepository.save(product);
        List<ProductImage> images = request.getImages().stream()
                .map(image -> ProductImage.builder().imagePath(image).build()
        ).collect(Collectors.toList());
        List<ProductColor> colors = request.getColors().stream()
                .map(productColorRequest -> productMapper.toProductColor(productColorRequest)
                ).collect(Collectors.toList());
        List<ProductSize> sizes = request.getSizes().stream()
                .map(size ->productSizeMapper.toProductSize(size)
                ).collect(Collectors.toList());
        List<Tag> tags = new ArrayList<>();
        for(String tagName: request.getTags()){
            Optional<Tag> tagOptional = tagRepository.findById(tagName);
            if(tagOptional.isEmpty()){
                Tag newTag = Tag.builder().name(tagName).build();
                tags.add(tagRepository.save(newTag));
            }else{
                tags.add(tagOptional.get());
            }
        }
        if(images.size() >0) {
            images = productImageRepository.saveAll(images);
            product.setImages(images);
        }
        if(colors.size() >0){
            colors = productColorRepository.saveAll(colors);
            product.setColors(colors);
        }
        if(sizes.size() >0){
            sizes = productSizeRepository.saveAll(sizes);
            product.setSizes(sizes);
        }
        for(Tag tag: tags){
            productTagRepositoty.save(ProductTag.builder().product(product).tag(tag).build());
        }
        for(ProductColor color:colors){
            for(ProductSize size: sizes){
                Optional<ProductDetail> productDetailOptional =
                        productDetailRepository.findByProductIdAndProductColorIdAndProductSizeId(product.getId(), color.getId(), size.getId());
                if(productDetailOptional.isEmpty()){
                    ProductDetail productDetail = ProductDetail.builder()
                            .product(product)
                            .productColor(color)
                            .productSize(size)
                            .price(product.getDefaultPrice())
                            .discount(product.getDefaultDiscount())
                            .build();
                    productDetailRepository.save(productDetail);

                }
            }
        }
        return productRepository.save(product);
    }
    public ProductImage addProductImage(AddProductImageRequest request){
        Optional<Product> productOptional = productRepository.findById(request.getProductId());
        if (productOptional.isEmpty())
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        ProductImage productImage = ProductImage.builder()
                .imagePath(request.getImagePath())
                .build();
        productImage = productImageRepository.save(productImage);
        if (productOptional.get().getImages() == null)
            productOptional.get().setImages(new ArrayList<>()) ;
        productOptional.get().getImages().add(productImage);
        productRepository.save(productOptional.get());
        return productImage;
    }
    public ProductResponse getByProductId(long productId){
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }else{
            return converToProductResponse(productOptional.get());
        }

    }
    public List<ProductResponse> getAll(){
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = products.stream()
                .map(product -> converToProductResponse(product)).collect(Collectors.toList());
        return productResponses;
    }
    public ProductResponse converToProductResponse(Product product){
        ProductResponse productResponse = productMapper.toProductResponse(product);
        List<ProductTag> productTags = productTagRepositoty.findByProductId(product.getId());
        List<ProductReview> productReviews = productReviewRepository.findByProductId(product.getId());
        int totalRating = productReviews.stream()
                .mapToInt(productReview -> productReview.getRatingScore())
                .sum();
        List<String> tags = productTags.stream().map(productTag -> (productTag.getTag().getName())).collect(Collectors.toList());

        productResponse.setTags(tags);
        for(String t: tags){
            log.info(t);
        }
//        List<ProductDetail> productDetails = productDetailRepository.findByProductId(product.getId());
//        List<ProductDetailRespone> productDetailResponses = productDetails.
//                stream().map(productDetail -> productMapper.toProductDetailResponse(productDetail)).collect(Collectors.toList());
//        productResponse.setDetails(productDetailResponses);
        productResponse.setTotalSale(getTotalSale(product.getId()));
        productResponse.setQuantity(getCurQuantity(product.getId()));
        productResponse.setNumReviews(productReviews.size());
        productResponse.setAvgRating(totalRating/Math.max(1, productReviews.size()));
        return productResponse;
    }


    public int getTotalSale(long productId){
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByProductIdAndOrderStatus(productId, OrderStatus.DA_HUY);
        int totalSale =0;
        for (OrderItem orderItem : orderItems) {
            totalSale += orderItem.getQuantity();
        }
        return totalSale;
    }
    public int getCurQuantity(long productId){
        List<ProductImport> productImports = productImportRepository.findByProductId(productId);
        int totalImport =0;
        for (ProductImport productImport: productImports){
            totalImport += productImport.getQuantity();
        }
        return totalImport - getTotalSale(productId);
    }
    public List<ProductResponse> getByName(String productName){
        List<Product> products = productRepository.findByName(productName);
        List<ProductResponse> productResponses = products.stream()
                .map(product -> converToProductResponse(product)).collect(Collectors.toList());
        return productResponses;
    }
    public ProductResponse updateProduct(ProductUpdateRequest request){
        Product product = productRepository.findById(request.getId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setDefaultPrice(request.getDefaultPrice());
        product.setDefaultDiscount(request.getDefaultDiscount());
        product.setPublished(request.isPublished());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return productMapper.toProductResponse(productRepository.save(product));
    }

}
