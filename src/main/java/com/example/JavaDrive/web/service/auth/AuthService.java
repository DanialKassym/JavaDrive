package com.example.JavaDrive.web.service.auth;

import com.example.JavaDrive.domain.entity.EmailToken;
import com.example.JavaDrive.domain.entity.Users;
import com.example.JavaDrive.domain.enums.RoleEnum;
import com.example.JavaDrive.domain.repository.EmailTokenRepository;
import com.example.JavaDrive.domain.repository.UserRepository;
import com.example.JavaDrive.utils.EmailValidate;
import com.example.JavaDrive.web.dto.AuthRequest;
import com.example.JavaDrive.web.dto.UserAuthDto;
import com.example.JavaDrive.web.service.infrastructure.Email;
import com.example.JavaDrive.web.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EmailTokenRepository emailTokenRepository;
    private final Email emailSender;
    @Value("${emailRegex}")
    private String emailRegex;


    @Transactional
    public boolean sendVerificationEmail(String userEmail){
        if (!EmailValidate.patternMatches(userEmail, emailRegex)) {
            return false;
        }
        String uuid = UUID.randomUUID().toString();
        String link = "http://localhost:8080/api/v1/auth/emailVerify/" + uuid;
        Date expiryDate = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
        emailTokenRepository.save(new EmailToken(uuid, userEmail, expiryDate, false));
        emailSender.sendSimpleEmail(userEmail, link);
        return true;
    }

    @Transactional
    public void registerUser(AuthRequest request,String email){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password_hash = encoder.encode(request.password);
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Users user = new Users(request.username, email, password_hash, date, RoleEnum.ROLE_USER);
        userRepository.save(user);
    }

    public boolean authenticateUser(AuthRequest request){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username, request.password));
        } catch (AuthenticationException e) {
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public UserAuthDto getUserData(AuthRequest request){
        String id = String.valueOf(userRepository.findByusername(request.username).get().getId());
        UserDetails userDetails = userService.loadUserByUsername(request.username);
        return new UserAuthDto(id,userDetails);
    }
}
