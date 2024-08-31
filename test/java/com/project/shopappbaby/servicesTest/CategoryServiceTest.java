package com.project.shopappbaby.servicesTest;
import com.project.shopappbaby.services.CategoryService;
import com.project.shopappbaby.dtos.CategoryDTO;
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory() {
        CategoryDTO categoryDTO = new CategoryDTO("New Category");
        Category category = Category.builder().name("New Category").build();

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.createCategory(categoryDTO);

        assertEquals(category.getName(), createdCategory.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getCategoryById() {
        Category category = Category.builder().name("Existing Category").build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getCategoryById(1L);

        assertEquals(category.getName(), foundCategory.getName());
        verify(categoryRepository, times(1)).findById(anyLong());
    }

    @Test
    void getCategoryByIdNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.getCategoryById(1L));
        verify(categoryRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllCategories() {
        categoryService.getAllCategories();
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void updateCategory() {
        Category existingCategory = Category.builder().name("Old Category").build();
        CategoryDTO categoryDTO = new CategoryDTO("Updated Category");

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        Category updatedCategory = categoryService.updateCategory(1L, categoryDTO);

        assertEquals(categoryDTO.getName(), updatedCategory.getName());
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void deleteCategory() {
        doNothing().when(categoryRepository).deleteById(anyLong());

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(anyLong());
    }
}

