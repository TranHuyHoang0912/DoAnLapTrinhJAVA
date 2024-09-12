package com.project.shopappbaby.controllers;
import com.project.shopappbaby.components.LocalizationUtils;
import com.project.shopappbaby.dtos.*;
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.models.Comment;
import com.project.shopappbaby.models.User;
import com.project.shopappbaby.responses.CategoryResponse;
import com.project.shopappbaby.responses.CommentResponse;
import com.project.shopappbaby.responses.UpdateCategoryResponse;
import com.project.shopappbaby.services.CategoryService;
import com.project.shopappbaby.services.CommentService;
import com.project.shopappbaby.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("${api.prefix}/comments")
//@Validated
//Dependency Injection
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("")
    public ResponseEntity<List<CommentResponse>> getAllComments(
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam("product_id") Long productId
    ) {
        List<CommentResponse> commentResponses;
        if (userId == null) {
            commentResponses = commentService.getCommentsByProduct(productId);
        } else {
            commentResponses = commentService.getCommentsByUserAndProduct(userId, productId);
        }
        return ResponseEntity.ok(commentResponses);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") Long commentId,
            @Valid @RequestBody CommentDTO commentDTO
    ) {
        try {
            User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!Objects.equals(loginUser.getId(), commentDTO.getUserId())) {
                return ResponseEntity.badRequest().body("You cannot update another user's comment");
            }
            commentService.updateComment(commentId, commentDTO);
            return ResponseEntity.ok("Update comment successfully");
        } catch (Exception e) {
            // Handle and log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during comment update.");
        }
    }
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> insertComment(
            @Valid @RequestBody CommentDTO commentDTO
    ) {
        try {
            // Chèn một comment của người dùng mới vào
            User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(loginUser.getId() != commentDTO.getUserId()) {
                return ResponseEntity.badRequest().body("You cannot comment as another user");
            }
            commentService.insertComment(commentDTO);
            return ResponseEntity.ok("Insert comment successfully");
        } catch (Exception e) {
            // Handle and log the exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("An error occurred during comment insertion.");
        }
    }
}
