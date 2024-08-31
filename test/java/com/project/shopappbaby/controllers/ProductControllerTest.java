package com.project.shopappbaby.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopappbaby.dtos.ProductDTO;
import com.project.shopappbaby.dtos.ProductImageDTO;
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.models.Product;
import com.project.shopappbaby.models.ProductImage;
import com.project.shopappbaby.responses.ProductListResponse;
import com.project.shopappbaby.responses.ProductResponse;
import com.project.shopappbaby.services.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    @Mock
    private BindingResult bindingResult;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateProduct_ValidInput() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Sữa cho mẹ bầu ");
        productDTO.setPrice(100.0f);
        productDTO.setDescription("Tốt cho sức khỏe");

        Product product = new Product();
        product.setId(1L);

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(product);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<?> response = productController.createProduct(productDTO, bindingResult);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(product, response.getBody());
    }

    @Test
    void testCreateProduct_InvalidInput() {
        ProductDTO productDTO = new ProductDTO();
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = productController.createProduct(productDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testUpdateProduct_ValidInput() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Sữa cho mẹ bầu và em bé");
        productDTO.setPrice(150.0f);
        productDTO.setDescription("Sản phẩm tốt cho sức khỏe");

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Sữa cho mẹ bầu và em bé");

        when(productService.updateProduct(anyLong(), any(ProductDTO.class))).thenReturn(updatedProduct);

        ResponseEntity<?> response = productController.updateProduct(1L, productDTO);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(updatedProduct, response.getBody());
    }

    @Test
    void testDeleteProduct_ValidId() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<String> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Product with id = 1 deleted successfully", response.getBody());
    }

    @Test
    void testGetProductById_ValidId() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Sữa cho mẹ bầu ");
        product.setPrice(100.0f);
        product.setDescription("Sản phẩm được tin dùng");

        Category category = new Category();
        category.setId(2L);
        product.setCategory(category);

        ProductResponse productResponse = ProductResponse.fromProduct(product);

        when(productService.getProductById(anyLong())).thenReturn(product);

        ResponseEntity<?> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(productResponse, response.getBody());
    }


//    @Test
//    void testUploadImages_ValidFiles() throws Exception {
//        Product product = new Product();
//        product.setId(1L);
//
//        // Prepare mock objects
//        when(productService.getProductById(anyLong())).thenReturn(product);
//
//        // Create a mock ProductImageDTO
//        ProductImageDTO productImageDTO = new ProductImageDTO();
//        productImageDTO.setProductId(product.getId());
//
//        // Mock the service call
//        when(productService.createProductImage(anyLong(), any(ProductImageDTO.class))).thenReturn(new ProductImage());
//
//        // Create a mock MultipartFile
//        MultipartFile mockFile = mock(MultipartFile.class);
//        when(mockFile.getSize()).thenReturn(1L);
//        when(mockFile.getContentType()).thenReturn("image/jpeg");
//
//        List<MultipartFile> files = new ArrayList<>();
//        files.add(mockFile);
//
//        // Perform the controller method call
//        ResponseEntity<?> response = productController.uploadImages(1L, files);
//
//        // Verify the results
//        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
//    }
//
//    @Test
//    void testUploadImages_InvalidFileSize() throws Exception {
//        Product product = new Product();
//        product.setId(1L);
//
//        // Prepare mock objects
//        when(productService.getProductById(anyLong())).thenReturn(product);
//
//        // Create a mock MultipartFile with invalid size
//        MultipartFile mockFile = mock(MultipartFile.class);
//        when(mockFile.getSize()).thenReturn(11 * 1024 * 1024L); // File size > 10MB
//
//        List<MultipartFile> files = new ArrayList<>();
//        files.add(mockFile);
//
//        // Perform the controller method call
//        ResponseEntity<?> response = productController.uploadImages(1L, files);
//
//        // Verify the results
//        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE.value(), response.getStatusCodeValue());
//    }
//
//    @Test
//    void testUploadImages_InvalidFileType() throws Exception {
//        Product product = new Product();
//        product.setId(1L);
//
//        // Prepare mock objects
//        when(productService.getProductById(anyLong())).thenReturn(product);
//
//        // Create a mock MultipartFile with invalid type
//        MultipartFile mockFile = mock(MultipartFile.class);
//        when(mockFile.getSize()).thenReturn(1L);
//        when(mockFile.getContentType()).thenReturn("text/plain"); // Invalid file type
//
//        List<MultipartFile> files = new ArrayList<>();
//        files.add(mockFile);
//
//        // Perform the controller method call
//        ResponseEntity<?> response = productController.uploadImages(1L, files);
//
//        // Verify the results
//        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), response.getStatusCodeValue());
//    }

    @Test
    void testGetProductsByIds() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        List<Product> products = List.of(product1, product2);

        when(productService.findProductsByIds(anyList())).thenReturn(products);

        ResponseEntity<?> response = productController.getProductsByIds("1,2");

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(products, response.getBody());
    }
}
