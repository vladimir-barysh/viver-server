package com.example.messengerserver.dto;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    // конструкторы, геттеры и сеттеры
    public RegisterRequest() {}

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
