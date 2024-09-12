package com.project.shopappbaby.controllers;

import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class HealthCheckControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private HealthCheckController healthCheckController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHealthCheck_Success() {
        // Setup mock response
        List<Category> mockCategories = Collections.singletonList(new Category()); // Tạo danh sách Category giả
        when(categoryService.getAllCategories()).thenReturn(mockCategories);

        // Call the method under test
        ResponseEntity<?> response = healthCheckController.healthCheck();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ok", response.getBody());
    }

    @Test
    void testHealthCheck_Failure() {
        // Setup mock to throw exception
        doThrow(new RuntimeException("Service failure")).when(categoryService).getAllCategories();

        // Call the method under test
        ResponseEntity<?> response = healthCheckController.healthCheck();

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("failed", response.getBody());
    }
}
