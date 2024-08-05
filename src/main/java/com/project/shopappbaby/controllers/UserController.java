package com.project.shopappbaby.controllers;

import com.project.shopappbaby.dtos.UserDTO;
import com.project.shopappbaby.dtos.UserLoginDTO;
import com.project.shopappbaby.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.shopappbaby.dtos.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password does not match");
            }
            userService.createUser(userDTO);
            return ResponseEntity.ok("Register successfully");
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
        // Trả về token trong response
        return ResponseEntity.ok(token);
    }
//    {
//            "fullname":"Tran Le Minh Tan",
//            "phone_number":"012345678",
//            "address":"Los Santos",
//            "password":"Tan201204",
//            "retype_password":"Tan201205",
//            "date_of_birth":"2004-12-20",
//            "facebook_account_id":0,
//            "google_account_id":0,
//            "role_id":1
//
//    }
}
