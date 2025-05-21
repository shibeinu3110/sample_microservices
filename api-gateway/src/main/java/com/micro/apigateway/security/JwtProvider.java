package com.micro.apigateway.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtProvider {
    String generateToken(String username, String role);

    String extractUsername(String token);

    Date extractExpiration(String token);

    boolean validateToken(String token, UserDetails userDetails);

    String extractRole(String token);
}
