package org.example.cdweb_be.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.AddProductImageRequest;
import org.example.cdweb_be.dto.request.ProductCreateRequest;
import org.example.cdweb_be.dto.request.ProductTagRequest;
import org.example.cdweb_be.dto.request.ProductUpdateRequest;
import org.example.cdweb_be.dto.response.ProductResponse;
import org.example.cdweb_be.entity.*;
import org.example.cdweb_be.enums.OrderStatus;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.ProductMapper;
import org.example.cdweb_be.mapper.ProductSizeMapper;
import org.example.cdweb_be.respository.*;
import org.example.cdweb_be.utils.IPUtils;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.sql.Timestamp;
import java.util.*;
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
    ProductTagRepository productTagRepository;
    OrderItemRepository orderItemRepository;
    ProductImportRepository productImportRepository;
    ProductReviewRepository productReviewRepository;
    ProductHistoryRepository productHistoryRepository;
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
            productTagRepository.save(ProductTag.builder().product(product).tag(tag).build());
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
    public List<ProductImage> addProductImages(AddProductImageRequest request){
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS)
        );
        if(request.getImagePaths().size() ==0) throw new AppException(ErrorCode.IMAGE_PAHTS_EMPTY);
        List<ProductImage> productImages = request.getImagePaths().stream()
                .map(imagePath -> productImageRepository.save(ProductImage.builder()
                        .imagePath(imagePath)
                        .build())).collect(Collectors.toList());
//        productImage = productImageRepository.save(productImage);
        if (product.getImages() == null)
            product.setImages(new ArrayList<>()) ;
        product.getImages().addAll(productImages);
        productRepository.save(product);
        return productImages;
    }
    public ProductResponse getByProductId(long productId, HttpServletRequest request){
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        }else{
            String ip = IPUtils.getIP(request);
            Optional<ProductHistory> productHistoryOptional = productHistoryRepository.findById(ip);
            ProductHistory productHistory;
            if(productHistoryOptional.isEmpty()){
                productHistory = ProductHistory.builder()
                        .ip(ip)
                        .products(new HashSet<Product>())
                        .build();

            }else{
                productHistory = productHistoryOptional.get();
            }
            productHistory.getProducts().remove(productOptional.get());
            productHistoryRepository.save(productHistory);
            productHistory.getProducts().add(productOptional.get());
            productHistoryRepository.save(productHistory);
            return converToProductResponse(productOptional.get());
        }

    }
    public List<ProductResponse> getHistory(HttpServletRequest request){
        String ip = IPUtils.getIP(request);
        Optional<ProductHistory > productHistoryOptional = productHistoryRepository.findById(ip);
        if(productHistoryOptional.isEmpty()){
            return new ArrayList<>();
        }else{
            List<ProductResponse> productResponses = productHistoryOptional.get().getProducts().stream()
                    .map(product -> converToProductResponse(product)).collect(Collectors.toList());
            return productResponses;
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
        List<ProductTag> productTags = productTagRepository.findByProductId(product.getId());
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
    public Product updateProduct(ProductUpdateRequest request){
        Product product = productRepository.findById(request.getId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        Category category = categoryRepository.findById(request.getCategoryId()).
                orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTS));
        product.setCategory(category);
        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setDefaultPrice(request.getDefaultPrice());
        product.setDefaultDiscount(request.getDefaultDiscount());
        product.setPublished(request.isPublished());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return productRepository.save(product);
    }
    public String deleteImage(long productId, long imageId){
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS)
        );
        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_EXISTS));
        if(product.getImages().contains(productImage)){
            product.getImages().remove(productImage);
            productRepository.save(product);
            return "Delete image success!";
        }else{
            throw new AppException(ErrorCode.IMAGE_INVALID);
        }
    }
    public List<String> addTags(ProductTagRequest request){
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        int curTags = productTagRepository.findByProductId(product.getId()).size();
        List<Tag> tags = new ArrayList<>();
        for(String tagName : request.getTagNames()){
            Optional<Tag> tagOptional = tagRepository.findById(tagName);
            if(tagOptional.isEmpty()){
                Tag newTag = Tag.builder().name(tagName).build();
                tags.add(tagRepository.save(newTag));
            }else{
                tags.add(tagOptional.get());
            }
        }
        for(Tag tag:tags){
            Optional<ProductTag> productTagOptional = productTagRepository
                    .findByProductIdAndTagName(product.getId(), tag.getName());
            if(productTagOptional.isEmpty()){
                ProductTag productTag = ProductTag.builder()
                        .product(product)
                        .tag(tag)
                        .build();
                productTagRepository.save(productTag);
            }else{
                tags.remove(tag);
            }
        }
        List<ProductTag> newTags = productTagRepository.findByProductId(product.getId());
        if(curTags == newTags.size()) throw new AppException(ErrorCode.ADD_TAG_FAILD);
        List<String> rs = tags
                .stream().map(tag -> tag.getName()).collect(Collectors.toList());
        return rs;
    }
    public List<String> deleteTags(ProductTagRequest request){
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        if(request.getTagNames().size() ==0 ) throw new AppException(ErrorCode.PRODUCT_TAG_EMPTY);
        List<ProductTag> productTags = new ArrayList<>();
        for (String tagName : request.getTagNames()) {
            Optional<ProductTag> productTagOptional = productTagRepository
                    .findByProductIdAndTagName(request.getProductId(), tagName);
            if(productTagOptional.isEmpty()){
                throw new AppException(ErrorCode.PRODUCT_TAG_NOT_EXISTS.setMessage(tagName +" is not a tag of ProductId"));
            }else{
                productTags.add(productTagOptional.get());
            }
        }
        productTagRepository.deleteAll(productTags);
        List<String> curTags = productTagRepository.findByProductId(product.getId())
                .stream().map(productTag -> productTag.getTag().getName()).collect(Collectors.toList());
        return curTags;
    }
    public List<ProductResponse> getByCategory(long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_EXISTS)
        );
        List<Product> products = productRepository.findByCategoryId(categoryId);
        List<ProductResponse> result = products.stream().map(
                product -> converToProductResponse(product)
        ).collect(Collectors.toList());
        return result;
    }
    public Set<ProductResponse> getSimilar(long productId){
        Set<ProductResponse> result = new HashSet<>();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        List<ProductResponse> productResponses = productRepository.findByName(product.getName()).stream()
                        .map(pr -> converToProductResponse(pr)).collect(Collectors.toList());
        result.addAll(productResponses);
        productResponses = productRepository.findByCategoryId(product.getCategory().getId()).stream()
                .map(pr -> converToProductResponse( pr)).collect(Collectors.toList());
        result.addAll(productResponses);
        return result;
    }
    public String deleteProduct(long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow( () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        productTagRepository.deleteAll(productTagRepository.findByProductId(productId));
        productDetailRepository.deleteAll(productDetailRepository.findByProductId(productId));
        productRepository.delete(product);
        return "Delete product success!";
    }
}
