package com.example.messengerserver.controller;

import com.example.messengerserver.request.RegisterRequest;
import com.example.messengerserver.request.LoginRequest;
import com.example.messengerserver.model.User;
import com.example.messengerserver.repository.UserRepository;
import com.example.messengerserver.services.JwtService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // для почты вводятся цифры и буквы - название почты и домен (минимум два символа)
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9]+@[a-z0-9]+\\.[a-z0-9]{2,}$");


    public AuthController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank() ||
                request.getEmail() == null || request.getEmail().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Все поля обязательны");
        }

        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            return ResponseEntity.badRequest().body("Некорректный email");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Эта почта уже зарегистрирована");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        String hashed = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(hashed);

        userRepository.save(user);
        String token = jwtService.generateToken(user.getId());

        return ResponseEntity.ok().body("Registered successfully. Token: " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .map(user -> {
                    if (BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
                        String token = jwtService.generateToken(user.getId());
                        return ResponseEntity.ok().body("Login successful. Token: " + token);
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }
}
