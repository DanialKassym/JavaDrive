package com.example.JavaDrive.Users;

import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;

    public  UserService(UserRepository userRepository,RolesRepository rolesRepository){
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }

    public Optional<Users> findByusername(String username){
        return userRepository.findByusername(username);
    }
    public Optional<Roles> findByName(String name){
        return rolesRepository.findByName(name);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = findByusername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("user not found ; %s", username)
        ));
        return new User(users.getUsername(),
                users.getPassword_hash(),
                users.getRoles().stream().map(roles -> new SimpleGrantedAuthority(roles.getName())).collect(Collectors.toList()));
    }

}
