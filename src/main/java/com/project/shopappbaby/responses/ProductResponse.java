package com.project.shopappbaby.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopappbaby.models.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
// trả về respone chỉ chứa mỗi category_id
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse{
    private String name;
    private Float price;
    private String urlProduct;
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;
    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .urlProduct(product.getUrlProduct())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
