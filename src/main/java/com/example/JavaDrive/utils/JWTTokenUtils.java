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
import java.util.List;

@Component
public class JWTTokenUtils {
    @Value("${jwt.secret}")
    private String secretkey;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public String generateJwtToken(UserDetails userDetails, String id) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        Date issueddate = new Date();
        Date expireddate = new Date(issueddate.getTime() + lifetime.toMillis());
        SecretKey key = Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().claim("id",id).claim("roles", roles)
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
    public boolean validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));
        try{
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e){
            return  false;
        }
    }
    public String getUsername(String token){
        return getClaimsFromToken(token).getSubject();
    }
    public String getID(String token){
        return getClaimsFromToken(token).get("id").toString();
    }
    public List<String> getRoles(String token){
        return getClaimsFromToken(token).get("roles", List.class);
    }
}
