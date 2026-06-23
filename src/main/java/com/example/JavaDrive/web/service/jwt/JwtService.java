package com.example.JavaDrive.web.service.jwt;

import com.example.JavaDrive.domain.entity.EmailToken;
import com.example.JavaDrive.domain.repository.EmailTokenRepository;
import com.example.JavaDrive.utils.JWTTokenUtils;
import com.example.JavaDrive.web.dto.UserAuthDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class JwtService {
    private final EmailTokenRepository emailTokenRepository;
    private final JWTTokenUtils jwtTokenUtils;

    public boolean ValidateEmail(String emailToken) {
        EmailToken token = emailTokenRepository.findBytoken(emailToken);
        return token != null && !token.getExpiry().before(new Date());
    }

    public List<String> EmailVerify(String emailToken){
        EmailToken token = emailTokenRepository.findBytoken(emailToken);

        token.setVerified(true);

        ResponseCookie tokenCookie = ResponseCookie.from("Token", token.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(20 * 60)
                .build();
        ResponseCookie emailCookie = ResponseCookie.from("Email", token.getEmail())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(20 * 60)
                .build();
        return List.of(tokenCookie.toString(), emailCookie.toString());
    }

    public boolean ValidateCookies(String tokenCookie,String emailCookie){
        EmailToken emailToken = emailTokenRepository.findBytoken(tokenCookie);
        EmailToken email = emailTokenRepository.findByEmail(emailCookie);
        return emailToken != null && email != null && !emailToken.getExpiry().before(new Date());
    }

    public ResponseCookie AuthorizeUser(UserAuthDto authDto){
        String token = jwtTokenUtils.generateJwtToken(authDto.userId(), authDto.userDetails());
        return ResponseCookie.from("JWT", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();
    }
}
