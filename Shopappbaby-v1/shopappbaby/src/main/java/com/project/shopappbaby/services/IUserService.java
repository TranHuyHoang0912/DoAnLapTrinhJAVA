package com.project.shopappbaby.services;

import com.project.shopappbaby.dtos.UpdateUserDTO;
import com.project.shopappbaby.dtos.UserDTO;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password, Long roleId) throws Exception;
    User getUserDetailsFromToken(String token) throws Exception;
    User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws Exception;
}
