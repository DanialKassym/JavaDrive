package com.example.JavaDrive.web.service.user;

import com.example.JavaDrive.domain.entity.Users;
import com.example.JavaDrive.domain.enums.RoleEnum;
import com.example.JavaDrive.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public  UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public Users findByid(Long id){
        return userRepository.findByid(id);
    }
    public Optional<Users> findByusername(String username){
        return userRepository.findByusername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = findByusername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("user not found ; %s", username)
        ));
        List<RoleEnum> authorities = List.of(RoleEnum.ROLE_USER);
        return new User(users.getUsername(),
                users.getPassword_hash(),
                authorities);
    }

}
