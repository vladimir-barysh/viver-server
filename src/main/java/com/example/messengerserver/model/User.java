package com.example.messengerserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID
    private Long userId;
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Password hash cannot be null")
    private String passwordHash;
    @NotNull(message = "email cannot be null")
    private String email;

    public User() {}
    public User(Long id, String username, String email, String passwordHash) {
        this.userId = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() { return userId; }
    public void setId(Long id) { this.userId = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}