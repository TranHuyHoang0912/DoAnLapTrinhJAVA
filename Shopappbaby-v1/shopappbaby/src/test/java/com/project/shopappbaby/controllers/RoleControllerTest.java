package com.project.shopappbaby.controllers;

import com.project.shopappbaby.models.Role;
import com.project.shopappbaby.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoles() {
        // Tạo dữ liệu mẫu cho test
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("user");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("admin");

        List<Role> roles = Arrays.asList(role1, role2);

        // Thiết lập mock cho roleService
        when(roleService.getAllRoles()).thenReturn(roles);

        // Gọi phương thức getAllRoles và kiểm tra kết quả
        ResponseEntity<?> response = roleController.getAllRoles();

        // Kiểm tra mã trạng thái HTTP
        assertEquals(200, response.getStatusCodeValue());

        // Kiểm tra nội dung của response
        List<Role> responseBody = (List<Role>) response.getBody();
        assertEquals(2, responseBody.size());
        assertEquals(role1.getName(), responseBody.get(0).getName());
        assertEquals(role2.getName(), responseBody.get(1).getName());
    }
}
