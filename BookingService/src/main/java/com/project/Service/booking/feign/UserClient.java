package com.project.Service.booking.feign;

import com.project.Service.booking.dto.UserResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/api/v1/user/{id}")
    @CircuitBreaker(name = "userService", fallbackMethod = "getUserByIdFallback")
    @Retry(name = "userService")
    UserResponseDTO getUserById(@PathVariable("id") Long userId);
    
    // Fallback method
    default UserResponseDTO getUserByIdFallback(Long userId, Exception ex) {
        UserResponseDTO fallbackUser = new UserResponseDTO();
        fallbackUser.setId(userId);
        fallbackUser.setEmail("unavailable@service.com");
        fallbackUser.setFirstName("Service");
        fallbackUser.setLastName("Unavailable");
        return fallbackUser;
    }
}
