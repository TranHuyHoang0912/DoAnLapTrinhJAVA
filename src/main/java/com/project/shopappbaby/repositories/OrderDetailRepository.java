package com.project.shopappbaby.repositories;

import com.project.shopappbaby.models.OrderDetail;
import com.project.shopappbaby.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository  extends JpaRepository<Product, Long> {

}
