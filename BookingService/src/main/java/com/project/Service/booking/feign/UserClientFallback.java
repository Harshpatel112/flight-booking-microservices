package com.project.Service.booking.feign;

import com.project.Service.booking.dto.UserResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserClientFallback implements UserClient {

    @Override
    public UserResponseDTO getUserById(Long userId) {
        log.warn("User service is unavailable. Using fallback for user ID: {}", userId);
        
        UserResponseDTO fallbackUser = new UserResponseDTO();
        fallbackUser.setId(userId);
        fallbackUser.setEmail("service.unavailable@booking.com");
        fallbackUser.setFirstName("Service");
        fallbackUser.setLastName("Unavailable");
        fallbackUser.setPhoneNumber("000-000-0000");
        
        return fallbackUser;
    }
}