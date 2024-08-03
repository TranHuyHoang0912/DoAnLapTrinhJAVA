package com.project.shopappbaby.controllers;

import com.project.shopappbaby.dtos.CategoryDTO;
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.services.CategoryService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
//dependency injection là một kỹ thuật theo đó một đối tượng (hoặc static method) cung cấp các phụ thuộc của đối tượng khác. Một phụ thuộc là một đối tượng có thể được sử dụng (service)."
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("")
    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
    public ResponseEntity<?> insertCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category successfully");

    }
    //Hiện tất cả các categories
    @GetMapping("") //http://localhost:7070/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page")     int page,
            @RequestParam("limit")    int limit
    ) {
        List<Category>categories= categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Update category successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("deleteCategory with id = "+id+" successfully");
    }
}
