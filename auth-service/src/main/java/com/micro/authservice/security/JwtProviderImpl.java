package com.micro.authservice.security;

import com.micro.authservice.config.SecretKeyProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

import static com.micro.authservice.consts.SecurityConst.EXPIRE_TIME;


@Service
@Slf4j(topic = "AUTH-SERVICE-JWT")
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {
    //    @Value("${jwt.secret}")
    private final SecretKeyProperties secretKeyProperties;
    private String secretKey;

    @PostConstruct
    void init() {
        this.secretKey = secretKeyProperties.getSecret();
        log.info("Secret key: {}", secretKey);
    }

    @Override
    public String generateToken(String username, String role) {
        log.info(secretKey);
        log.info("Generating token for user: {}", username);
        log.info("Role: {}", role);
        return Jwts.builder()
                .claim("role", role)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(getKey())
                .compact();
    }


    private SecretKey getKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    //extract username từ token
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //validate username trong token và trong user details
    //userDetails sẽ là 1 interface bao gồm cac thông tin về user (username, password, authorities)
    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build().parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
}
