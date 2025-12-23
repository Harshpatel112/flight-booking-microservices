package com.project.User.Service;

import com.project.User.Repository.UserRepository;
import com.project.User.enm.Role;
import com.project.User.model.User;
import com.project.User.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(User user) {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return "User registered successfully";
    }

    // 🔥 Full Login Logic Here
    public ResponseEntity<?> login(String identifier, String password, String type) {
        Optional<User> optionalUser = Optional.empty();

        if ("email".equalsIgnoreCase(type)) {
            optionalUser = repository.findByEmail(identifier);
        } else if ("username".equalsIgnoreCase(type)) {
            optionalUser = repository.findByUsername(identifier);
        }

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
                Map<String, Object> response = Map.of(
                    "token", token,
                    "role", user.getRole().name()
                );
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of("error", "Invalid " + type + " or password"));
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }
}
