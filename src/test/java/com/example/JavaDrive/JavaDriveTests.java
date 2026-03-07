package com.example.JavaDrive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class JavaDriveTests {

    @Autowired
    private MockMvc mockMvc;

}