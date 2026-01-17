package com.example.JavaDrive;

import com.example.JavaDrive.Users.UserController;
import com.example.JavaDrive.Users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class JavaDriveTests {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private UserRepository userRepository;
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    public void shouldReturnHelloWorld() throws Exception {
        mockMvc.perform(get("/unsecure"))
                .andExpect(content().string(containsString("you are on public")))
                .andExpect(status().is2xxSuccessful());
    }

    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    @Test
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
        mockMvc.perform(get("/admin").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(containsString("you are on admin")));
    }
}