package com.example.MainApplication.controllers;

import com.example.MainApplication.models.Users;
import com.example.MainApplication.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/get/{username}")
    public String hello(@PathVariable("username") String username) {
        return "hello";
    }


    @PostMapping("/create")
    public Users createUser(@RequestBody Users users) {
        return repository.save(users);
    }

    @GetMapping("/users")
    public List<Users> getUsers() {
        return repository.findAll();
    }
}
