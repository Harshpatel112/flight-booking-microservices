package com.project.User.Service;

import com.project.User.dto.LoginRequestDTO;
import com.project.User.dto.LoginResponseDTO;
import com.project.User.dto.UserRegistrationDTO;
import com.project.User.dto.UserResponseDTO;

public interface UserService {
    
    // User Registration & Authentication
    LoginResponseDTO registerUser(UserRegistrationDTO registrationDTO);
    LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO);
    
    // User Profile Management
    UserResponseDTO getUserProfileByToken(String token);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUserProfile(String token, UserRegistrationDTO updateDTO);
    
    // Token Management
    LoginResponseDTO refreshToken(String refreshToken);
    void logoutUser(String token);
}