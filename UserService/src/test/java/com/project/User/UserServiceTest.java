package com.project.User;

import com.project.User.Repository.UserRepository;
import com.project.User.Service.UserService;
import com.project.User.enm.Role;
import com.project.User.exception.UserNotFoundException;
import com.project.User.model.User;
import com.project.User.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUsername("john");
        testUser.setEmail("john@example.com");
        testUser.setPassword("pass123");
        testUser.setRole(Role.USER);
    }

    @Test
    void testRegister_Success() {
        when(repository.findByUsername("john")).thenReturn(Optional.empty());
        when(repository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");

        String response = userService.register(testUser);

        assertEquals("User registered successfully", response);
        assertEquals("encodedPass", testUser.getPassword());
        verify(repository, times(1)).save(testUser);
    }

    @Test
    void testLoginWithUsername_Success() {
        when(repository.findByUsername("john")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("pass123", "pass123")).thenReturn(true);
        when(jwtUtil.generateToken("john", "USER")).thenReturn("mockedToken");

        ResponseEntity<?> response = userService.login("john", "pass123", "username");

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("mockedToken", responseBody.get("token"));
        assertEquals("USER", responseBody.get("role"));
    }

    @Test
    void testLoginWithEmail_Success() {
        when(repository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("pass123", "pass123")).thenReturn(true);
        when(jwtUtil.generateToken("john", "USER")).thenReturn("mockedToken");

        ResponseEntity<?> response = userService.login("john@example.com", "pass123", "email");

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("mockedToken", responseBody.get("token"));
        assertEquals("USER", responseBody.get("role"));
    }

    @Test
    void testLogin_UserNotFound() {
        when(repository.findByUsername("invalid")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.login("invalid", "anyPass", "username");

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("error").toString().contains("Invalid"));
    }

    @Test
    void testLogin_EmailNotFound() {
        when(repository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.login("invalid@example.com", "anyPass", "email");

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("error").toString().contains("Invalid"));
    }

    @Test
    void testLogin_WrongPassword() {
        when(repository.findByUsername("john")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrong", "pass123")).thenReturn(false);

        ResponseEntity<?> response = userService.login("john", "wrong", "username");

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("error").toString().contains("Invalid"));
    }

    @Test
    void testGetUserByUsername_Success() {
        when(repository.findByUsername("john")).thenReturn(Optional.of(testUser));

        User user = userService.getUserByUsername("john");

        assertEquals("john", user.getUsername());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        when(repository.findByUsername("notExist")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserByUsername("notExist");
        });
    }

    @Test
    void testExtractUsername() {
        when(jwtUtil.extractUsername("token")).thenReturn("john");

        String result = userService.extractUsername("token");

        assertEquals("john", result);
    }
}
