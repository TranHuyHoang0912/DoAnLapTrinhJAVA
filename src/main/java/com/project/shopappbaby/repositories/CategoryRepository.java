package com.project.shopappbaby.repositories;
// Repository là nơi chứa các hàm để thao tác xuống models và từ models thao tác xuống Database,ví dụ thêm mới cập nhật 1 thực thể, tác dụng: với mỗi thực thể sẽ cập nhật xuống Database
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
