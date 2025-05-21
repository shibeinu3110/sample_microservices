package com.micro.authservice.service.impl;

import com.micro.authservice.message.request.SignInRequest;
import com.micro.authservice.message.request.SignUpRequest;
import com.micro.authservice.message.response.SignUpResponse;
import com.micro.authservice.message.response.TokenResponse;
import com.micro.authservice.model.User;
import com.micro.authservice.repository.RoleRepository;
import com.micro.authservice.repository.UserRepository;
import com.micro.authservice.security.JwtProvider;

import com.micro.authservice.service.AuthenticationService;
import com.micro.authservice.validator.UserValidator;
import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserValidator userValidator;

    private final String ACCESS_TOKEN="redisAccessToken";
    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        userValidator.checkDuplicateUsername(signUpRequest.getUsername());

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setRoleId(signUpRequest.getRoleId().toString());
        String role = roleRepository.findById(signUpRequest.getRoleId().toString()).get().getRole();
        userRepository.save(user);
        return SignUpResponse.builder()
                .token(jwtProvider.generateToken(user.getUsername(), role))
                .name(user.getUsername())
                .roleId(user.getRoleId())
                .roleName(roleRepository.findById(user.getRoleId()).get().getRole())
                .build();
    }

    @Override
    public SignUpResponse signIn(SignInRequest signInRequest) {
        SignUpResponse sign = new SignUpResponse();
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        log.info("Authorities: {}", authentication.getAuthorities());

        if (authentication.isAuthenticated()) {
            log.info("User {} authenticated successfully", signInRequest.getUsername());
            sign.setName(signInRequest.getUsername());


            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            log.info("Roles: {}", roles);

            sign.setRoleName(roles.get(0));
            String redisToken = jwtProvider.generateToken(signInRequest.getUsername(), roles.get(0));
            sign.setToken(jwtProvider.generateToken(signInRequest.getUsername(), roles.get(0)));

            redisTemplate.opsForValue().set(ACCESS_TOKEN + signInRequest.getUsername(), redisToken, 30, TimeUnit.MINUTES);
            log.info("Redis token: {} saved", redisToken);
        } else {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "Authentication failed");
        }
        return sign;
    }

    @Override
    public TokenResponse getRefreshToken(HttpServletRequest refreshToken) {
        return null;
    }

    @Override
    public String logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        log.info("Logout request received with header: {}", authHeader);

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtProvider.extractUsername(token);
            redisTemplate.delete(ACCESS_TOKEN + username);
            return "Logout successful";
        }

        return null;
    }
}
