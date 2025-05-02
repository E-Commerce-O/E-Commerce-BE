package org.example.cdweb_be.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.example.cdweb_be.dto.request.*;
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

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class ProductService {
    TagRepository tagRepository;
    CategoryRepository categoryRepository;
    OrderItemRepository orderItemRepository;
    ProductDetailService productDetailService;
    ProductRepository productRepository;
    ProductSizeRepository productSizeRepository;
    ProductColorRepository productColorRepository;
    ProductMapper productMapper;
    ProductReviewService productReviewService;
    ProductImageRepository productImageRepository;
    ProductDetailRepository productDetailRepository;
    ProductTagRepository productTagRepository;
    ProductImportRepository productImportRepository;
    ProductReviewRepository productReviewRepository;
    ProductHistoryRepository productHistoryRepository;

    public ProductResponse addProduct(ProductCreateRequest request) {
        Product product = productMapper.toProduct(request);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Optional<Category> categoryOptional = categoryRepository.findById(request.getCategoryId());
        if (categoryOptional.isEmpty())
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTS);
        product.setCategory(categoryOptional.get());
        product = productRepository.save(product);
        List<ProductImage> images = new ArrayList<>();
        for (String image : request.getImages()) {
            images.add(ProductImage.builder()
                    .product(product)
                    .imagePath(image)
                    .build());
        }
        List<ProductColor> productColors = new ArrayList<>();
        for (ColorRequest colorRequest : request.getColors()) {
            ProductColor productColor = productMapper.toProductColor(colorRequest);
            productColor.setProduct(product);
            productColors.add(productColor);
        }
        List<ProductSize> sizes = new ArrayList<>();
        for (SizeCreateRequest sizeCreateRequest : request.getSizes()) {
            sizes.add(ProductSize.builder()
                    .product(product)
                    .description(sizeCreateRequest.getDescription())
                    .size(sizeCreateRequest.getSize())
                    .build());
        }
        List<Tag> tags = new ArrayList<>();
        for (String tagName : request.getTags()) {
            Optional<Tag> tagOptional = tagRepository.findById(tagName);
            if (tagOptional.isEmpty()) {
                Tag newTag = Tag.builder().name(tagName).build();
                tags.add(tagRepository.save(newTag));
            } else {
                tags.add(tagOptional.get());
            }
        }
        if (images.size() > 0) {
            images = productImageRepository.saveAll(images);
        }
        if (productColors.size() > 0) {
            productColors = productColorRepository.saveAll(productColors);
//            product.setColors(productColors);
        }
        if (sizes.size() > 0) {
            sizes = productSizeRepository.saveAll(sizes);
//            product.setSizes(sizes);
        }
        for (Tag tag : tags) {
            productTagRepository.save(ProductTag.builder().product(product).tag(tag).build());
        }
        if (!productColors.isEmpty() || !sizes.isEmpty()) {
            productDetailService.initDetail(product, productColors, sizes);

        }

        return converToProductResponse(product);
    }

    public List<ProductImage> addProductImages(AddProductImageRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS)
        );

        if (request.getImagePaths().size() == 0) throw new AppException(ErrorCode.IMAGE_PAHTS_EMPTY);
        List<ProductImage> images = request.getImagePaths().stream()
                .map(imagePath -> productImageRepository.save(ProductImage.builder()
                        .product(product)
                        .imagePath(imagePath)
                        .build())).collect(Collectors.toList());
        productRepository.save(product);
        return images;
    }

    public ProductResponse getByProductId(long productId, HttpServletRequest request) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
        } else {
            String ip = IPUtils.getIP(request);
            Optional<ProductHistory> productHistoryOptional = productHistoryRepository.findByIpAndProductId(ip, productId);
            ProductHistory productHistory;
            if (productHistoryOptional.isEmpty()) {
                productHistory = ProductHistory.builder()
                        .ip(ip)
                        .product(productOptional.get())
                        .viewAt(new Timestamp(System.currentTimeMillis()))
                        .build();

            } else {
                productHistory = productHistoryOptional.get();
                productHistory.setViewAt(new Timestamp(System.currentTimeMillis()));
            }
            productHistoryRepository.save(productHistory);
            return converToProductResponse(productOptional.get());
        }

    }

    public List<ProductResponse> getHistory(HttpServletRequest request) {
        String ip = IPUtils.getIP(request);

        List<ProductResponse> productResponses = productHistoryRepository.findByIp(ip).stream()
                .map(product -> converToProductResponse(product)).collect(Collectors.toList());
        return productResponses;

    }

    public List<ProductResponse> getAll() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = products.stream()
                .map(product -> converToProductResponse(product)).collect(Collectors.toList());
        return productResponses;
    }

    public ProductResponse converToProductResponse(Product product) {
        ProductResponse productResponse = productMapper.toProductResponse(product);
        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
        productResponse.setImages(productImages.stream().map(productMapper::toProductImageResponse).collect(Collectors.toList()));
        List<ProductTag> productTags = productTagRepository.findByProductId(product.getId());
        List<ProductReview> productReviews = productReviewRepository.findByProductId(product.getId());
        int totalRating = productReviews.stream()
                .mapToInt(productReview -> productReview.getRatingScore())
                .sum();
        List<String> tags = productTags.stream().map(productTag -> (productTag.getTag().getName())).collect(Collectors.toList());
        List<ProductColor> productColors = productColorRepository.findByProductId(product.getId());
        productResponse.setColors(productColors.stream().map(productMapper::toProductColorResponse).collect(Collectors.toList()));
        List<ProductSize> sizes = productSizeRepository.findByProductId(product.getId());
        productResponse.setSizes(sizes.stream().map(productMapper::toProductSizeResponse).collect(Collectors.toList()));
        productResponse.setTags(tags);
        productResponse.setTotalSale(getTotalSale(product.getId()));
        productResponse.setQuantity(getCurQuantity(product.getId()));
        productResponse.setNumReviews(productReviews.size());
        productResponse.setAvgRating(totalRating / Math.max(1, productReviews.size()));
        return productResponse;
    }


    public int getTotalSale(long productId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductIdAndExceptStatus(productId, OrderStatus.ST_DA_HUY);
        int totalSale = 0;
        for (OrderItem orderItem : orderItems) {
            totalSale += orderItem.getQuantity();
        }
        return totalSale;
    }

    public int getCurQuantity(long productId) {
        List<ProductImport> productImports = productImportRepository.findByProductId(productId);
        int totalImport = 0;
        for (ProductImport productImport : productImports) {
            totalImport += productImport.getQuantity();
        }
        return totalImport - getTotalSale(productId);
    }

    public List<ProductResponse> getByName(String productName) {
        List<Product> products = productRepository.findByName(productName);
        List<ProductResponse> productResponses = products.stream()
                .map(product -> converToProductResponse(product)).collect(Collectors.toList());
        return productResponses;
    }

    public Product updateProduct(ProductUpdateRequest request) {
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

    public String deleteImage(long productId, long imageId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS)
        );
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_EXISTS));
        if (image.getProduct().getId() == product.getId()) {
            productImageRepository.delete(image);
            return "Delete image success!";
        } else {
            throw new AppException(ErrorCode.IMAGE_INVALID);
        }
    }

    public List<String> addTags(ProductTagRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        int curTags = productTagRepository.findByProductId(product.getId()).size();
        List<Tag> tags = new ArrayList<>();
        for (String tagName : request.getTagNames()) {
            Optional<Tag> tagOptional = tagRepository.findById(tagName);
            if (tagOptional.isEmpty()) {
                Tag newTag = Tag.builder().name(tagName).build();
                tags.add(tagRepository.save(newTag));
            } else {
                tags.add(tagOptional.get());
            }
        }
        for (Tag tag : tags) {
            Optional<ProductTag> productTagOptional = productTagRepository
                    .findByProductIdAndTagName(product.getId(), tag.getName());
            if (productTagOptional.isEmpty()) {
                ProductTag productTag = ProductTag.builder()
                        .product(product)
                        .tag(tag)
                        .build();
                productTagRepository.save(productTag);
            } else {
                tags.remove(tag);
            }
        }
        List<ProductTag> newTags = productTagRepository.findByProductId(product.getId());
        if (curTags == newTags.size()) throw new AppException(ErrorCode.ADD_TAG_FAILD);
        List<String> rs = tags
                .stream().map(tag -> tag.getName()).collect(Collectors.toList());
        return rs;
    }

    public List<String> deleteTags(ProductTagRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        if (request.getTagNames().size() == 0) throw new AppException(ErrorCode.PRODUCT_TAG_EMPTY);
        List<ProductTag> productTags = new ArrayList<>();
        for (String tagName : request.getTagNames()) {
            Optional<ProductTag> productTagOptional = productTagRepository
                    .findByProductIdAndTagName(request.getProductId(), tagName);
            if (productTagOptional.isEmpty()) {
                throw new AppException(ErrorCode.PRODUCT_TAG_NOT_EXISTS.setMessage(tagName + " is not a tag of ProductId"));
            } else {
                productTags.add(productTagOptional.get());
            }
        }
        productTagRepository.deleteAll(productTags);
        List<String> curTags = productTagRepository.findByProductId(product.getId())
                .stream().map(productTag -> productTag.getTag().getName()).collect(Collectors.toList());
        return curTags;
    }

    public List<ProductResponse> getByCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_EXISTS)
        );
        List<Product> products = productRepository.findByCategoryId(categoryId);
        List<ProductResponse> result = products.stream().map(
                product -> converToProductResponse(product)
        ).collect(Collectors.toList());
        return result;
    }

    public Set<ProductResponse> getSimilar(long productId) {
        Set<ProductResponse> result = new HashSet<>();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        List<ProductResponse> productResponses = productRepository.findByName(product.getName()).stream()
                .map(pr -> converToProductResponse(pr)).collect(Collectors.toList());
        result.addAll(productResponses);
        productResponses = productRepository.findByCategoryId(product.getCategory().getId()).stream()
                .map(pr -> converToProductResponse(pr)).collect(Collectors.toList());
        result.addAll(productResponses);
        return result;
    }

    public String deleteProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        productTagRepository.deleteAll(productTagRepository.findByProductId(productId));
        productDetailRepository.deleteAll(productDetailRepository.findByProductId(productId));
        productRepository.delete(product);
        return "Delete product success!";
    }

    @Transactional
    public List<ProductColor> addColors(long productId, List<ColorRequest> requests) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS)
        );
        if ((productColorRepository.findByProductId(productId).isEmpty())
                && !productImportRepository.findByProductId(productId).isEmpty()) {
            throw new AppException(ErrorCode.CANT_ADD_COLOR);
        }
        List<ProductColor> productColors = new ArrayList<>();
        for (ColorRequest request : requests) {
            ProductColor productColor = ProductColor.builder()
                    .colorName(request.getColorName())
                    .colorCode(request.getColorCode())
                    .product(product)
                    .build();
            productColors.add(productColor);
            if (productColorRepository.findByProductAndName(productId, request.getColorName()).isPresent())
                throw new AppException(ErrorCode.COLOR_EXIDTED.
                        setMessage("ColorName " + request.getColorName() + " is already exists!"));
        }
        productColors = productColorRepository.saveAll(productColors);
//        product.getColors().addAll(productColors);
//        productRepository.save(product);
        productDetailService.initDetail(product, productColors, productSizeRepository.findByProductId(productId));
        productDetailRepository.deleteNullColor(productId);
        return productColors;
    }

    @Transactional
    public List<ProductSize> addSizes(long productId, List<SizeCreateRequest> requests) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        if ((productSizeRepository.findByProductId(productId).isEmpty())
                && !productImportRepository.findByProductId(productId).isEmpty()) {
            throw new AppException(ErrorCode.CANT_ADD_SIZE);
        }
        List<ProductSize> sizes = new ArrayList<>();
        for (SizeCreateRequest sizeRequest : requests) {
            ProductSize size = ProductSize.builder()
                    .product(product)
                    .size(sizeRequest.getSize())
                    .description(sizeRequest.getDescription())
                    .build();
            sizes.add(size);
            if (productSizeRepository.findByProductAndName(productId, sizeRequest.getSize()).isPresent())
                throw new AppException(ErrorCode.SIZE_EXIDTED.setMessage("Size " + sizeRequest.getSize() + " is already exists!"));

        }
        sizes = productSizeRepository.saveAll(sizes);
//        product.getSizes().addAll(sizes);
        productRepository.save(product);
        productDetailService.initDetail(product,
                productColorRepository.findByProductId(productId), sizes);
        productDetailRepository.deleteNullSize(productId);
        return sizes;
    }

    public int getRemainingQuantityByAllInfo(long productId, long sizeId, long colorId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductAndColorAndSizeAndExceptStatus(
                productId, colorId, sizeId, OrderStatus.ST_DA_HUY);
        List<ProductImport> productImports = productImportRepository.findByProductAndColorAndSize(
                productId, colorId, sizeId);
        log.info(productImports.size() + "");
        for (ProductImport productImport : productImports) {
            log.info(productImport.toString());
        }
        int totalImport = productImports.stream().mapToInt(
                ProductImport::getQuantity
        ).sum();
        int totalSale = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        return (totalImport - totalSale > 0) ? totalImport - totalSale : 0;
    }

    public int getRemainingQuantity(long productId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductIdAndExceptStatus(productId, OrderStatus.ST_DA_HUY);
        List<ProductImport> productImports = productImportRepository.findByProductId(
                productId);
        int totalImport = productImports.stream().mapToInt(
                ProductImport::getQuantity
        ).sum();
        int totalSale = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        return (totalImport - totalSale > 0) ? totalImport - totalSale : 0;
    }

    public int getRemainingQuantityWithoutColor(long productId, long sizeId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductAndSizeAndExceptStatus(
                productId, sizeId, OrderStatus.ST_DA_HUY);
        List<ProductImport> productImports = productImportRepository.findByProductAndSize(
                productId, sizeId);
        int totalImport = productImports.stream().mapToInt(
                ProductImport::getQuantity
        ).sum();
        int totalSale = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        return (totalImport - totalSale > 0) ? totalImport - totalSale : 0;
    }

    public int getRemainingQuantityWithoutSize(long productId, long colorId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductAndColorAndExceptStatus(
                productId, colorId, OrderStatus.ST_DA_HUY);
        List<ProductImport> productImports = productImportRepository.findByProductAndColor(
                productId, colorId);
        int totalImport = productImports.stream().mapToInt(
                ProductImport::getQuantity
        ).sum();
        int totalSale = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        return (totalImport - totalSale > 0) ? totalImport - totalSale : 0;
    }

    public double getPrice(Product product, ProductSize size, ProductColor color) {
        if (size == null && color == null) {
            return product.getDefaultPrice();
        } else {
            return productDetailService.getPrice(product, size, color);
        }

    }

    public int getDiscount(Product product, ProductSize size, ProductColor color) {
        if (size == null && color == null) {
            return product.getDefaultDiscount();
        } else {
            return productDetailService.getDiscount(product, size, color);
        }

    }

    public int getRemainingQuantity(Product product, ProductSize size, ProductColor color) {
        if (size == null && color == null) {

            return productDetailService.getRemainingQuantity(product.getId());
        } else {
            return productDetailService.getRemainingQuantity(product, size, color);
        }

    }
}
