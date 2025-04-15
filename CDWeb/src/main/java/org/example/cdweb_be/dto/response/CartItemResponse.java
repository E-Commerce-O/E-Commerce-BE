package org.example.cdweb_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cdweb_be.entity.CartItem;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.entity.ProductColor;
import org.example.cdweb_be.entity.ProductSize;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    long id;
    long productId;
    String productName;
    String slug;
    double price;
    int discount;
    boolean published;
    Category category;
    ProductColorResponse color;
    ProductSizeResponse size;
    List<String> images;
    String description;
    String brand;
    int cartItemQuantity;
    int productQuantity;

    public CartItemResponse(CartItem cartItem){
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.slug = cartItem.getProduct().getSlug();
//        this.price = cartItem.get
        this.published = cartItem.getProduct().isPublished();
        this.category = cartItem.getProduct().getCategory();
        if(cartItem.getColor() != null)
            this.color = ProductColorResponse.builder()
                .colorCode(cartItem.getColor().getColorCode())
                .colorName(cartItem.getColor().getColorName())
                .id(cartItem.getColor().getId())
                .build();

        if(cartItem.getSize() != null)
            this.size = ProductSizeResponse.builder()
                .id(cartItem.getSize().getId())
                .size(cartItem.getSize().getSize())
                .description(cartItem.getSize().getDescription())
                .build();
//        this.images = cartItem.getProduct().getImages().stream().map(image -> image.getImagePath()).collect(Collectors.toList());
        this.description = cartItem.getProduct().getDescription();
        this.brand = cartItem.getProduct().getBrand();
        this.cartItemQuantity = cartItem.getQuantity();

    }

}
