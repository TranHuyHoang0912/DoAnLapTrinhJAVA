package com.project.shopappbaby.controllers;

import com.project.shopappbaby.components.LocalizationUtils;
import com.project.shopappbaby.dtos.*;
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.models.Role;
import com.project.shopappbaby.responses.CategoryResponse;
import com.project.shopappbaby.responses.UpdateCategoryResponse;
import com.project.shopappbaby.services.CategoryService;
import com.project.shopappbaby.services.RoleService;
import com.project.shopappbaby.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor

public class RoleController {
    private final RoleService roleService;
    @GetMapping("")
    public ResponseEntity<?> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
