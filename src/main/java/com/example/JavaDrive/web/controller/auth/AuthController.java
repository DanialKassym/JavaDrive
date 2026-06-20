package com.example.JavaDrive.web.controller.auth;

import com.example.JavaDrive.domain.entity.EmailToken;
import com.example.JavaDrive.domain.entity.Users;
import com.example.JavaDrive.domain.enums.RoleEnum;
import com.example.JavaDrive.domain.repository.EmailTokenRepository;
import com.example.JavaDrive.domain.repository.UserRepository;
import com.example.JavaDrive.utils.EmailValidate;
import com.example.JavaDrive.utils.JWTTokenUtils;
import com.example.JavaDrive.web.dto.AuthRequest;
import com.example.JavaDrive.web.dto.EmailAddress;
import com.example.JavaDrive.web.service.Email;
import com.example.JavaDrive.web.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@RestController
public class AuthController {
    private final UserService userService;
    private final JWTTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EmailTokenRepository emailTokenRepository;
    private final Email emailSender;
    @Value("${emailRegex}")
    private String emailRegex;
    @Value("${server.port}")
    private String port;
    @Autowired
    public AuthController(UserService userService, JWTTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager,
                          UserRepository userRepository, EmailTokenRepository emailTokenRepository, Email emailSender) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.emailTokenRepository = emailTokenRepository;
        this.emailSender = emailSender;
    }
    @PostConstruct
    public void init() {
        System.out.println("The server port is: " + port);
    }
    @PostMapping("/emailConfirmation")
    @Transactional
    public ResponseEntity<?> EmailConfirmation(@RequestBody @Valid EmailAddress emailAddress) {
        String userEmail = emailAddress.getEmail();
        if (!EmailValidate.patternMatches(userEmail, emailRegex)) {
            return ResponseEntity.badRequest().build();
        }
        String uuid = UUID.randomUUID().toString();
        String link = "http://localhost:8081/emailVerify/" + uuid;
        Date expiryDate = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
        emailTokenRepository.save(new EmailToken(uuid, userEmail, expiryDate, false));
        emailSender.sendSimpleEmail(userEmail, link);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/emailVerify/{emailToken}")
    @Transactional
    public void EmailVerify(@PathVariable("emailToken") String emailToken, HttpServletResponse response) throws IOException {
        EmailToken token = emailTokenRepository.findBytoken(emailToken);
        if (token == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested resource was not found.");
            return;
        }
        if (token.getExpiry().before(new Date())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested resource was not found.");
            return;
        }
        token.setVerified(true);

        Cookie tokenCookie = new Cookie("Token", token.getToken());
        tokenCookie.setMaxAge(20 * 60);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");

        Cookie emailCookie = new Cookie("Email", token.getEmail());
        emailCookie.setMaxAge(20 * 60);
        emailCookie.setHttpOnly(true);
        emailCookie.setPath("/");

        response.addCookie(tokenCookie);
        response.addCookie(emailCookie);

        response.sendRedirect("/static/create-account.html");
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> Register(@Valid @RequestBody AuthRequest request, @CookieValue(name = "Token") String token,
                                           @CookieValue(name = "Email") String email, HttpServletResponse response) {
        EmailToken emailToken = emailTokenRepository.findBytoken(token);
        EmailToken email2 = emailTokenRepository.findByEmail(email);
        if (emailToken == null || email2 == null) {
            return ResponseEntity.badRequest().build();
        }
        if (emailToken.getExpiry().before(new Date())) {
            return ResponseEntity.badRequest().build();
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password_hash = encoder.encode(request.password);
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Users user = new Users(request.username, email, password_hash, date,RoleEnum.ROLE_USER);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> Authenticate(@RequestBody AuthRequest request, HttpServletResponse response) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username, request.password));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String id = String.valueOf(userRepository.findByusername(request.username).get().getId());
        UserDetails userDetails = userService.loadUserByUsername(request.username);
        String token = jwtTokenUtils.generateJwtToken(userDetails, id);
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
