package com.example.JavaDrive.web.controller.auth;

import com.example.JavaDrive.web.dto.AuthRequest;
import com.example.JavaDrive.web.dto.EmailAddress;
import com.example.JavaDrive.web.dto.UserAuthDto;
import com.example.JavaDrive.web.service.auth.AuthService;
import com.example.JavaDrive.web.service.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/emailConfirmation")
    public ResponseEntity<?> confirmEmail(@RequestBody @Valid EmailAddress emailAddress) {
        String userEmail = emailAddress.getEmail();
        if (!authService.sendVerificationEmail(userEmail)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/emailVerify/{emailToken}")
    public ResponseEntity<Void> verifyEmail(@PathVariable("emailToken") String emailToken) throws IOException {
        if (!jwtService.ValidateEmail(emailToken)){
            ResponseEntity.badRequest().build();
        }
        List<String> Cookies = jwtService.EmailVerify(emailToken);

        var responseBuilder = ResponseEntity.status(HttpStatus.FOUND);

        responseBuilder.header(HttpHeaders.LOCATION, "/static/create-account.html");

        for (String cookieString : Cookies) {
            responseBuilder.header(HttpHeaders.SET_COOKIE, cookieString);
        }

        return responseBuilder.build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> Register(@Valid @RequestBody AuthRequest request, @CookieValue(name = "Token") String tokenCookie,
                                           @CookieValue(name = "Email") String emailCookie) {
        if (!jwtService.ValidateCookies(tokenCookie,emailCookie)){
            return ResponseEntity.badRequest().build();
        }
        authService.registerUser(request,emailCookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> Authenticate(@Valid @RequestBody AuthRequest request) {

        if (!authService.authenticateUser(request)){
            return ResponseEntity.badRequest().build();
        }

        UserAuthDto authDto = authService.getUserData(request);
        ResponseCookie cookie = jwtService.AuthorizeUser(authDto);

        var responseBuilder = ResponseEntity.status(HttpStatus.OK);

        responseBuilder.header(HttpHeaders.SET_COOKIE, String.valueOf(cookie));

        return responseBuilder.build();
    }
}
