package com.example.JavaDrive.Users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/unsecure")
    public ResponseEntity<String> Unsecure(){
        return ResponseEntity.ok("you are on public");
    }
    @GetMapping("/secure")
    public ResponseEntity<String> Secure(){
        return ResponseEntity.ok("you are on public");
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
