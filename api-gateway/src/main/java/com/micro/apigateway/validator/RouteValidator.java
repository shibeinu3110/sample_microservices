package com.micro.apigateway.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    private static final List<String> openEndpoints = List.of(
            "/auth/sign-up", "/auth/sign-in"
    );

    public Predicate<ServerHttpRequest> isOpenRoute() {
        return request -> openEndpoints.stream().anyMatch(uri -> request.getURI().getPath().contains(uri));
    }
}
