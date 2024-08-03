package com.project.shopappbaby.repositories;

import com.project.shopappbaby.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber); // kiểm tra giá trị null ,isEmpty() là gia tri null còn isPresent() là khác null
}
