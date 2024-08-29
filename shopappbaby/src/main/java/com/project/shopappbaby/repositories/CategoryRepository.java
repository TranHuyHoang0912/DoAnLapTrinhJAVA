package com.project.shopappbaby.repositories;

import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
