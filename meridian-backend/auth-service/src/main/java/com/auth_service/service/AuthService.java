package com.auth_service.service;

import com.auth_service.dto.AuthResponse;
import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}