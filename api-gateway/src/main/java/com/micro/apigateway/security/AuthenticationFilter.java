package com.micro.apigateway.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.apigateway.validator.RouteValidator;
import com.micro.commonlib.common.StandardResponse;
import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "GATEWAY-FILTER")
public class AuthenticationFilter implements GlobalFilter {
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RouteValidator routeValidator;
    private final ObjectMapper objectMapper;
    private final String ACCESS_TOKEN="redisAccessToken";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Request Path: {}", exchange.getRequest().getPath());

        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        // default routes which are not authenticated
        if (routeValidator.isOpenRoute().test(serverHttpRequest)) {
            log.info("Open route: {}", serverHttpRequest.getPath());
            return chain.filter(exchange);
        }

        log.info("Need to check authentication for route: {}", serverHttpRequest.getPath());
        // handle token
        if(!serverHttpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return this.onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED.value());
        }

        String authHeader = serverHttpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return this.onError(exchange, "Invalid Authorization Header", HttpStatus.UNAUTHORIZED.value());
        }
        String token = authHeader.substring(7);
        String username = jwtProvider.extractUsername(token);
        String role = jwtProvider.extractRole(token);
        if(redisTemplate.opsForValue().get(ACCESS_TOKEN + username) == null ||
                !redisTemplate.opsForValue().get(ACCESS_TOKEN + username).equals(token)){
            return this.onError(exchange, "Token expired or not found", HttpStatus.UNAUTHORIZED.value());
        }


        if (username == null || role == null) {
            return this.onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED.value());
        }


        log.info("Username: {}", username);
        log.info("Role: {}", role);

        ServerHttpRequest request = serverHttpRequest.mutate()
                .header("username", username)
                .header("role", role)
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }



    //handle pretty error response
    private Mono<Void> onError(ServerWebExchange exchange, String message, int statusCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(statusCode));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        StandardResponse errorResponse = StandardResponse.build(ErrorMessages.ACCESS_DENIED, message);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(errorResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            bytes = "{\"message\":\"Unable to process error\"}".getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
