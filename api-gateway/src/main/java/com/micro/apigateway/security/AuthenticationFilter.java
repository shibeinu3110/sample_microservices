package com.micro.apigateway.security;

import com.micro.apigateway.validator.RouteValidator;
import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
@Slf4j(topic = "GATEWAY-FILTER")
public class AuthenticationFilter implements GlobalFilter {
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RouteValidator routeValidator;
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
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "Missing Authorization Header");
        }

        String authHeader = serverHttpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "Invalid Authorization Header");
        }
        String token = authHeader.substring(7);
        String username = jwtProvider.extractUsername(token);
        String role = jwtProvider.extractRole(token);
        if(redisTemplate.opsForValue().get(ACCESS_TOKEN + username)==null &&
                !redisTemplate.opsForValue().get(ACCESS_TOKEN + username).equals(token)){
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "Token not found");
        }


        if (username == null || role == null) {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "Invalid token");
        }


        log.info("Username: {}", username);
        log.info("Role: {}", role);

        ServerHttpRequest request = serverHttpRequest.mutate()
                .header("username", username)
                .header("role", role)
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }
}
