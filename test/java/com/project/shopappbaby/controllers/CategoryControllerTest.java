package com.project.shopappbaby.controllers;

import com.project.shopappbaby.components.LocalizationUtils;
import com.project.shopappbaby.dtos.CategoryDTO;
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.responses.CategoryResponse;
import com.project.shopappbaby.responses.UpdateCategoryResponse;
import com.project.shopappbaby.services.CategoryService;
import com.project.shopappbaby.utils.MessageKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private LocalizationUtils localizationUtils;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCategory_ValidData() {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO(); // Thiết lập DTO hợp lệ
        Category mockCategory = new Category();  // Tạo một đối tượng Category giả
        mockCategory.setId(1L); // Bạn có thể thiết lập các thuộc tính khác cho đối tượng giả

        // Mock các phương thức
        when(bindingResult.hasErrors()).thenReturn(false); // Không có lỗi xác thực
        when(categoryService.createCategory(categoryDTO)).thenReturn(mockCategory); // Trả về đối tượng Category giả
        when(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY))
                .thenReturn("Insert category successfully");

        // When
        ResponseEntity<CategoryResponse> response = categoryController.createCategory(categoryDTO, bindingResult);

        // Then
        verify(categoryService, times(1)).createCategory(categoryDTO); // Xác minh phương thức được gọi 1 lần
        assertEquals(200, response.getStatusCodeValue()); // Kiểm tra mã trạng thái HTTP
        assertNotNull(response.getBody()); // Đảm bảo body không null
        assertEquals("Insert category successfully", response.getBody().getMessage()); // Kiểm tra thông báo
        assertEquals(mockCategory, response.getBody().getCategory()); // Kiểm tra đối tượng Category
    }


    @Test
    public void testCreateCategory_InvalidData() {
        CategoryDTO categoryDTO = new CategoryDTO();
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("categoryDTO", "name", "Name is required")));
        when(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED)).thenReturn("Insert category failed");

        ResponseEntity<CategoryResponse> response = categoryController.createCategory(categoryDTO, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Insert category failed", response.getBody().getMessage());
        verify(categoryService, never()).createCategory(any(CategoryDTO.class));
    }

    @Test
    public void testGetAllCategories() {
        List<Category> mockCategories = List.of(new Category(), new Category());
        when(categoryService.getAllCategories()).thenReturn(mockCategories);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories(1, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCategories, response.getBody());
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    public void testUpdateCategory() {
        Long id = 1L;
        CategoryDTO categoryDTO = new CategoryDTO();
        when(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY)).thenReturn("Update category successfully");

        ResponseEntity<UpdateCategoryResponse> response = categoryController.updateCategory(id, categoryDTO);

        verify(categoryService, times(1)).updateCategory(id, categoryDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Update category successfully", response.getBody().getMessage());
    }

    @Test
    public void testDeleteCategory() {
        Long id = 1L;
        when(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY)).thenReturn("Delete category successfully");

        ResponseEntity<String> response = categoryController.deleteCategory(id);

        verify(categoryService, times(1)).deleteCategory(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Delete category successfully", response.getBody());
    }
}
