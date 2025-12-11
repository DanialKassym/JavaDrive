package com.example.JavaDrive.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JWTTokenUtils {
    @Value("${jwt.secret}")
    private String secretkey;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", roles);
        Date issueddate = new Date();
        Date expireddate = new Date(issueddate.getTime() + lifetime.toMillis());
        SecretKey key = Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims().issuer("http://localhost:8081").subject(userDetails.getUsername()).issuedAt(issueddate).expiration(expireddate).and()
                .signWith(key).compact();
    }

    private Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key).
                build().
                parseSignedClaims(token)
                .getPayload();
    }
    public String getUsername(String token){
        return getClaimsFromToken(token).getSubject();
    }
    public List<String> getRoles(String token){
        return getClaimsFromToken(token).get("roles", List.class);
    }
}
