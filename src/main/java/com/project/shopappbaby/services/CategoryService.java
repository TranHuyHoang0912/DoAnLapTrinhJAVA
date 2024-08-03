package com.project.shopappbaby.services;
import com.project.shopappbaby.dtos.CategoryDTO;
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newcategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newcategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId,CategoryDTO categoryDTO) {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
