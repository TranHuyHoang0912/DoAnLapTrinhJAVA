package com.project.shopappbaby.services;
import com.project.shopappbaby.dtos.ProductDTO;
import com.project.shopappbaby.dtos.ProductImageDTO;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.project.shopappbaby.models.*;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(long id) throws Exception;
    public Page<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId, PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id);
    boolean existsByName(String name);
    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception;

    List<Product> findProductsByIds(List<Long> productIds);
}