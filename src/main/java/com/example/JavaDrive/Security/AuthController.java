package com.example.JavaDrive.Security;

import com.example.JavaDrive.Email.Email;
import com.example.JavaDrive.Users.*;
import com.example.JavaDrive.utils.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final JWTTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EmailTokenRepository emailTokenRepository;
    private final Email emailSender;
    @Value("${emailRegex}")
    private String emailRegex;

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

    @PostMapping("/emailConfirmation")
    public ResponseEntity<?> EmailConfirmation(@RequestBody @Valid EmailAddress emailAddress) {
        String userEmail = emailAddress.getEmail();
        if (!EmailValidate.patternMatches(userEmail, emailRegex)) {
            return ResponseEntity.badRequest().build();
        }
        String uuid = uuidUtils.generateUniqueToken();
        String link = "http://localhost:8081/emailVerify/" + uuid;
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(15);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        emailTokenRepository.save(new EmailToken(uuid, userEmail, date, false));
        emailSender.sendSimpleEmail(userEmail, link);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/emailVerify/{emailToken}")
    @Transactional
    public ResponseEntity<String> EmailVerify(@PathVariable("emailToken") String emailToken, HttpServletResponse response) {
        EmailToken token = emailTokenRepository.findBytoken(emailToken);
        if (token == null) {
            return ResponseEntity.badRequest().build();
        }
        if (token.getExpiry().before(new Date())) {
            return ResponseEntity.badRequest().build();
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
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/content/register.html"));
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody JwtRequest request,@CookieValue(name = "Token",required = true) String token,
        @CookieValue(name = "Email",required = true) String email,HttpServletResponse response) {
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

        Users user = new Users(request.username, email, password_hash, date);
        userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/content/login.html"));
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> Authenticate(@RequestBody JwtRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username, request.password));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(request.username);
        String token = jwtTokenUtils.generateJwtToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
