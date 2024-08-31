package com.project.shopappbaby.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopappbaby.components.LocalizationUtils;
import com.project.shopappbaby.dtos.UpdateUserDTO;
import com.project.shopappbaby.dtos.UserDTO;
import com.project.shopappbaby.dtos.UserLoginDTO;
import com.project.shopappbaby.models.User;
import com.project.shopappbaby.responses.LoginResponse;
import com.project.shopappbaby.responses.RegisterResponse;
import com.project.shopappbaby.responses.UserResponse;
import com.project.shopappbaby.services.IUserService;
import com.project.shopappbaby.utils.MessageKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private LocalizationUtils localizationUtils;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUser_ValidInput() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setPhoneNumber("012345678");
        userDTO.setPassword("password");
        userDTO.setRetypePassword("password");

        User user = new User();
        user.setId(1L);

        when(userService.createUser(any(UserDTO.class))).thenReturn(user);
        when(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY)).thenReturn("Register successfully");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<RegisterResponse> response = userController.createUser(userDTO, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Register successfully", response.getBody().getMessage());
        assertEquals(user, response.getBody().getUser());
    }

    @Test
    void testCreateUser_PasswordMismatch() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("password");
        userDTO.setRetypePassword("Password");

        when(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH)).thenReturn("Password does not match");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<RegisterResponse> response = userController.createUser(userDTO, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Password does not match", response.getBody().getMessage());
    }

    @Test
    void testCreateUser_InvalidInput() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setPhoneNumber("012345678");
        userDTO.setPassword("password");
        userDTO.setRetypePassword("password");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        FieldError fieldError = new FieldError("userDTO", "phoneNumber", "Phone number is invalid");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<RegisterResponse> response = userController.createUser(userDTO, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().getMessage().contains("Phone number is invalid"));
    }

    @Test
    void testLogin_ValidCredentials() throws Exception {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setPhoneNumber("012345678");
        userLoginDTO.setPassword("password");
        userLoginDTO.setRoleId(1L);

        String token = "validToken";

        when(userService.login(anyString(), anyString(), anyLong())).thenReturn(token);
        when(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY)).thenReturn("Login successfully");

        ResponseEntity<LoginResponse> response = userController.login(userLoginDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login successfully", response.getBody().getMessage());
        assertEquals(token, response.getBody().getToken());
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setPhoneNumber("012345678");
        userLoginDTO.setPassword("wrongPassword");
        userLoginDTO.setRoleId(1L);

        when(userService.login(anyString(), anyString(), anyLong())).thenThrow(new RuntimeException("Invalid credentials"));
        when(localizationUtils.getLocalizedMessage(eq(MessageKeys.LOGIN_FAILED), anyString()))
                .thenReturn("Login failed: Invalid credentials");

        ResponseEntity<LoginResponse> response = userController.login(userLoginDTO);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Login failed: Invalid credentials", response.getBody().getMessage());
    }
    @Test
    void testGetUserDetails_ValidToken() throws Exception {
        String token = "validToken";
        User user = new User();
        user.setId(1L);
        user.setPhoneNumber("012345678");

        when(userService.getUserDetailsFromToken(token)).thenReturn(user);

        String authorizationHeader = "Bearer " + token;
        ResponseEntity<UserResponse> response = userController.getUserDetails(authorizationHeader);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getPhoneNumber(), response.getBody().getPhoneNumber());
    }

    @Test
    void testGetUserDetails_InvalidToken() throws Exception {
        String token = "invalidToken";

        when(userService.getUserDetailsFromToken(token)).thenThrow(new RuntimeException("Invalid token"));

        String authorizationHeader = "Bearer " + token;
        ResponseEntity<UserResponse> response = userController.getUserDetails(authorizationHeader);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() == null);
    }

    @Test
    void testUpdateUserDetails_ValidUser() throws Exception {
        String token = "validToken";
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setPhoneNumber("012345678");

        UpdateUserDTO updatedUserDTO = new UpdateUserDTO();
        updatedUserDTO.setPhoneNumber("098765432");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setPhoneNumber(updatedUserDTO.getPhoneNumber());

        when(userService.getUserDetailsFromToken(token)).thenReturn(user);
        when(userService.updateUser(userId, updatedUserDTO)).thenReturn(updatedUser);

        String authorizationHeader = "Bearer " + token;
        ResponseEntity<UserResponse> response = userController.updateUserDetails(userId, updatedUserDTO, authorizationHeader);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUser.getPhoneNumber(), response.getBody().getPhoneNumber());
    }

    @Test
    void testUpdateUserDetails_UnauthorizedUser() throws Exception {
        String token = "validToken";
        Long userId = 1L;

        User user = new User();
        user.setId(2L); // Different user ID

        UpdateUserDTO updatedUserDTO = new UpdateUserDTO();
        updatedUserDTO.setPhoneNumber("098765432");

        when(userService.getUserDetailsFromToken(token)).thenReturn(user);

        String authorizationHeader = "Bearer " + token;
        ResponseEntity<UserResponse> response = userController.updateUserDetails(userId, updatedUserDTO, authorizationHeader);

        assertEquals(403, response.getStatusCodeValue());
    }

    @Test
    void testUpdateUserDetails_InvalidToken() throws Exception {
        String token = "invalidToken";
        Long userId = 1L;

        UpdateUserDTO updatedUserDTO = new UpdateUserDTO();
        updatedUserDTO.setPhoneNumber("098765432");

        when(userService.getUserDetailsFromToken(token)).thenThrow(new RuntimeException("Invalid token"));

        String authorizationHeader = "Bearer " + token;
        ResponseEntity<UserResponse> response = userController.updateUserDetails(userId, updatedUserDTO, authorizationHeader);

        assertEquals(400, response.getStatusCodeValue());
    }

}
