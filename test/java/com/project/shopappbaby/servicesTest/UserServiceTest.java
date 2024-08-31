package com.project.shopappbaby.servicesTest;

import com.project.shopappbaby.services.UserService;
import com.project.shopappbaby.components.JwtTokenUtils;
import com.project.shopappbaby.components.LocalizationUtils;
import com.project.shopappbaby.dtos.UpdateUserDTO;
import com.project.shopappbaby.dtos.UserDTO;
import com.project.shopappbaby.exceptions.DataNotFoundException;
import com.project.shopappbaby.exceptions.PermissionDenyException;
import com.project.shopappbaby.models.Role;
import com.project.shopappbaby.models.User;
import com.project.shopappbaby.repositories.RoleRepository;
import com.project.shopappbaby.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenUtils jwtTokenUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private LocalizationUtils localizationUtils;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setPhoneNumber("123456789");
        userDTO.setPassword("password");
        userDTO.setRoleId(1L);
        userDTO.setFullName("John Doe");

        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        when(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())).thenReturn(false);
        when(roleRepository.findById(userDTO.getRoleId())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        User user = userService.createUser(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.getPhoneNumber(), user.getPhoneNumber());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserPhoneNumberExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPhoneNumber("123456789");

        when(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())).thenReturn(true);

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userService.createUser(userDTO);
        });

        assertEquals("Phone number already exists", exception.getMessage());
    }

    @Test
    void testLoginSuccess() throws Exception {
        String phoneNumber = "123456789";
        String password = "password";
        Long roleId = 1L;

        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword("encodedPassword");
        user.setRole(new Role());
        user.setActive(true);

        // Mocking the repository behavior
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // Mocking role repository
        Role role = new Role();
        role.setId(roleId);
        role.setName("USER");
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Mocking JWT token utility
        when(jwtTokenUtil.generateToken(user)).thenReturn("jwtToken");

        // Executing the login method
        String token = userService.login(phoneNumber, password, roleId);

        // Assertions
        assertEquals("jwtToken", token);
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        Long userId = 1L;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setPhoneNumber("987654321");
        updateUserDTO.setFullName("Jane Doe");

        User existingUser = new User();
        existingUser.setPhoneNumber("123456789");
        existingUser.setFullName("John Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByPhoneNumber(updateUserDTO.getPhoneNumber())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        User updatedUser = userService.updateUser(userId, updateUserDTO);

        assertNotNull(updatedUser);
        assertEquals(updateUserDTO.getPhoneNumber(), updatedUser.getPhoneNumber());
        assertEquals(updateUserDTO.getFullName(), updatedUser.getFullName());
    }

    @Test
    void testGetUserDetailsFromTokenSuccess() throws Exception {
        String token = "validToken";
        String phoneNumber = "123456789";

        User user = new User();
        user.setPhoneNumber(phoneNumber);

        when(jwtTokenUtil.isTokenExpired(token)).thenReturn(false);
        when(jwtTokenUtil.extractPhoneNumber(token)).thenReturn(phoneNumber);
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));

        User retrievedUser = userService.getUserDetailsFromToken(token);

        assertEquals(phoneNumber, retrievedUser.getPhoneNumber());
    }
}
