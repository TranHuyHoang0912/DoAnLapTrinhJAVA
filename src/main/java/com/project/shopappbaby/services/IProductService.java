package com.project.shopappbaby.services;
import com.project.shopappbaby.dtos.ProductDTO;
import com.project.shopappbaby.dtos.ProductImageDTO;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.exceptions.InvalidParamException;
import com.project.shopappbaby.models.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

public interface IProductService {

    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(long id) throws Exception;

    Page<Product> getAllProducts(PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(long id);

    boolean existsByName(String name);
    //
    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;
}
