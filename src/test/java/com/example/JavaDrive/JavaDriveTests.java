package com.example.JavaDrive;

import com.example.JavaDrive.web.service.Email;
import com.example.JavaDrive.web.controller.AuthController;
import com.example.JavaDrive.domain.repository.EmailTokenRepository;
import com.example.JavaDrive.domain.repository.RolesRepository;
import com.example.JavaDrive.domain.repository.UserRepository;
import com.example.JavaDrive.web.service.UserService;
import com.example.JavaDrive.utils.JWTTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class JavaDriveTests {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private final UserService userService;
    private final JWTTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EmailTokenRepository emailTokenRepository;
    private final Email emailSender;
    private final RolesRepository rolesRepository;

    @Autowired
    public JavaDriveTests(UserService userService, JWTTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager, UserRepository userRepository, EmailTokenRepository emailTokenRepository, Email emailSender, RolesRepository rolesRepository) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.emailTokenRepository = emailTokenRepository;
        this.emailSender = emailSender;
        this.rolesRepository = rolesRepository;
    }

    @Test
    public void testGetRequest() throws Exception {
        mockMvc.perform(get("/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}