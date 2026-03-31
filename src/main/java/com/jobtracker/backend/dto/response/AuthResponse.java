package com.jobtracker.backend.dto.response;

public class AuthResponse {

    private String token;
    private String email;
    private String fullName;

    public AuthResponse(String token, String email, String fullName) {
        this.token = token;
        this.email = email;
        this.fullName = fullName;
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }

    public void setToken(String token) { this.token = token; }
    public void setEmail(String email) { this.email = email; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}