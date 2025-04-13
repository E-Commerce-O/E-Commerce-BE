package org.example.cdweb_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.entity.Color;
import org.example.cdweb_be.entity.Size;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    long id;
    long productId;
    String name;
    String slug;
    double price;
    int discount;
    boolean published;
    Category category;
    Color color;
    Size size;
    List<String> images;
    String description;
    String brand;
    int cartItemQuantity;
    int productQuantity;

}
