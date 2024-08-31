package com.project.shopappbaby.servicesTest;

import com.project.shopappbaby.dtos.ProductDTO;
import com.project.shopappbaby.dtos.ProductImageDTO;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.exceptions.InvalidParamException;
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.models.Product;
import com.project.shopappbaby.models.ProductImage;
import com.project.shopappbaby.repositories.CategoryRepository;
import com.project.shopappbaby.repositories.ProductImageRepository;
import com.project.shopappbaby.repositories.ProductRepository;
import com.project.shopappbaby.responses.ProductResponse;
import com.project.shopappbaby.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @InjectMocks
    private ProductService productService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() throws DataNotFoundException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setPrice(100.0F);
        productDTO.setUrl_product("http://example.com/product");
        productDTO.setDescription("Test Description");
        productDTO.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .url_product(productDTO.getUrl_product())
                .description(productDTO.getDescription())
                .category(category)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(productDTO);

        assertNotNull(result);
        assertEquals(productDTO.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProductCategoryNotFound() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> productService.createProduct(productDTO));
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.getDetailProduct(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).getDetailProduct(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.getDetailProduct(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).getDetailProduct(1L);
    }

    @Test
    void testFindProductsByIds() {
        List<Long> productIds = List.of(1L, 2L, 3L);
        List<Product> products = new ArrayList<>();

        when(productRepository.findProductsByIds(productIds)).thenReturn(products);

        List<Product> result = productService.findProductsByIds(productIds);

        assertNotNull(result);
        assertEquals(products, result);
        verify(productRepository, times(1)).findProductsByIds(productIds);
    }

    @Test
    void testGetAllProducts() {
        String keyword = "test";
        Long categoryId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Product> products = new ArrayList<>();
        Page<Product> productsPage = new PageImpl<>(products);

        when(productRepository.searchProducts(categoryId, keyword, pageRequest)).thenReturn(productsPage);

        Page<ProductResponse> result = productService.getAllProducts(keyword, categoryId, pageRequest);

        assertNotNull(result);
        assertEquals(products.size(), result.getTotalElements());
        verify(productRepository, times(1)).searchProducts(categoryId, keyword, pageRequest);
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setCategoryId(1L);

        Product existingProduct = new Product();
        existingProduct.setId(1L);

        Category category = new Category();
        category.setId(1L);

        when(productRepository.getDetailProduct(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.updateProduct(1L, productDTO);

        assertNotNull(result);
        assertEquals(productDTO.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategoryId(1L);

        when(productRepository.getDetailProduct(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> productService.updateProduct(1L, productDTO));
        verify(productRepository, times(1)).getDetailProduct(1L);
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testCreateProductImage() throws Exception {
        ProductImageDTO productImageDTO = new ProductImageDTO();
        productImageDTO.setImageUrl("http://example.com/image");
        productImageDTO.setProductId(1L);

        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImageRepository.findByProductId(1L)).thenReturn(new ArrayList<>());

        ProductImage productImage = ProductImage.builder()
                .product(product)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        when(productImageRepository.save(any(ProductImage.class))).thenReturn(productImage);

        ProductImage result = productService.createProductImage(1L, productImageDTO);

        assertNotNull(result);
        assertEquals(productImageDTO.getImageUrl(), result.getImageUrl());
        verify(productImageRepository, times(1)).save(any(ProductImage.class));
    }

    @Test
    void testCreateProductImageExceedsLimit() {
        ProductImageDTO productImageDTO = new ProductImageDTO();
        productImageDTO.setImageUrl("http://example.com/image");
        productImageDTO.setProductId(1L);

        Product product = new Product();
        product.setId(1L);

        List<ProductImage> existingImages = new ArrayList<>();
        for (int i = 0; i < ProductImage.MAXIMUM_IMAGES_PER_PRODUCT; i++) {
            existingImages.add(new ProductImage());
        }

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImageRepository.findByProductId(1L)).thenReturn(existingImages);

        assertThrows(InvalidParamException.class, () -> productService.createProductImage(1L, productImageDTO));
        verify(productImageRepository, never()).save(any(ProductImage.class));
    }

    @Test
    void tearDown() throws Exception {
        closeable.close();
    }
}

