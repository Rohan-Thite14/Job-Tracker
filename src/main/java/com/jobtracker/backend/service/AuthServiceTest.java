package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.request.LoginRequest;
import com.jobtracker.backend.dto.request.RegisterRequest;
import com.jobtracker.backend.dto.response.AuthResponse;
import com.jobtracker.backend.entity.User;
import com.jobtracker.backend.repository.UserRepository;
import com.jobtracker.backend.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Rohan Thite");
        request.setEmail("rohan@test.com");
        request.setPassword("123456");

        when(userRepository.existsByEmail("rohan@test.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtUtil.generateToken("rohan@test.com")).thenReturn("mock.jwt.token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("rohan@test.com", response.getEmail());
        assertEquals("Rohan Thite", response.getFullName());
        assertEquals("mock.jwt.token", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("rohan@test.com");
        request.setPassword("123456");
        request.setFullName("Rohan Thite");

        when(userRepository.existsByEmail("rohan@test.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register(request));
        assertEquals("Email already registered", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("rohan@test.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail("rohan@test.com");
        user.setFullName("Rohan Thite");
        user.setPassword("hashed");

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail("rohan@test.com"))
                .thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("rohan@test.com")).thenReturn("mock.jwt.token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("rohan@test.com", response.getEmail());
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@test.com");
        request.setPassword("123456");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail("unknown@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }
}