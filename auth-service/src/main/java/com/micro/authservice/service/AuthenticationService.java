package com.micro.authservice.service;

import com.micro.authservice.message.request.SignInRequest;
import com.micro.authservice.message.request.SignUpRequest;
import com.micro.authservice.message.response.SignUpResponse;
import com.micro.authservice.message.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.InputStream;

public interface AuthenticationService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
    SignUpResponse signIn(SignInRequest signInRequest);
    TokenResponse getRefreshToken(HttpServletRequest refreshToken);

    String logout(String authHeader);
}
