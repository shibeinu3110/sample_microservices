package com.micro.authservice.controller;

import com.micro.authservice.message.request.SignInRequest;
import com.micro.authservice.message.request.SignUpRequest;
import com.micro.authservice.message.response.SignUpResponse;
import com.micro.authservice.message.response.TokenResponse;
import com.micro.authservice.service.AuthenticationService;
import com.micro.commonlib.common.StandardResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.micro.authservice.consts.SecurityConst.SECRET_STRENGTH;

@RestController
@RequestMapping("/auth")
@Slf4j(topic = "AUTH-CONTROLLER")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(SECRET_STRENGTH);

    @GetMapping("/sign-up")
    public StandardResponse<SignUpResponse> auth(@RequestBody SignUpRequest sign) {
        sign.setPassword(passwordEncoder.encode(sign.getPassword()));
        return StandardResponse.build(authenticationService.signUp(sign));
    }

    @PostMapping("/sign-in")
    public StandardResponse<SignUpResponse> login(@RequestBody SignInRequest sign) {
        return StandardResponse.build(authenticationService.signIn(sign));
    }

    @PostMapping("/logout")
    public StandardResponse<String> logout(HttpServletRequest request) {
        log.info("Logout request received: {}", request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        return StandardResponse.build(authenticationService.logout(authHeader));
    }
}
