package com.project.shopappbaby.services;

import com.project.shopappbaby.dtos.CategoryDTO;
import com.project.shopappbaby.models.Category;
import com.project.shopappbaby.models.Role;
import com.project.shopappbaby.repositories.CategoryRepository;
import com.project.shopappbaby.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
