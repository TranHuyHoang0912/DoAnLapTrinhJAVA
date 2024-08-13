package com.project.shopappbaby.services;

import com.project.shopappbaby.components.JwtTokenUtil;
import com.project.shopappbaby.dtos.UserDTO;
import com.project.shopappbaby.exceptions.DataNotFoundException;

import com.project.shopappbaby.exceptions.PermissionDenyException;
import com.project.shopappbaby.models.*;
import com.project.shopappbaby.repositories.RoleRepository;
import com.project.shopappbaby.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        //register user
        String phoneNumber = userDTO.getPhoneNumber();
        // Kiểm tra xem số điện thoại đã tồn tại hay chưa
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role =roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You cannot register an admin account");
        }
        //chuyển đổi từ userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        newUser.setRole(role);
        // Kiểm tra nếu có accountId, không yêu cầu password
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number / password");
        }
        //return optionalUser.get();//muốn trả JWT token ?
        User existingUser = optionalUser.get();
        //kiểm tra password
        if (existingUser.getFacebookAccountId() == 0
                && existingUser.getGoogleAccountId() == 0) {
            if(!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password,
                existingUser.getAuthorities()
        );

        //authenticationManager của Java Spring security,đối tượng chịu trách nhiệm quản lý việc xác thực người dùng trong Spring Security, tronmg trường hợp này là authenticationToken
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}













