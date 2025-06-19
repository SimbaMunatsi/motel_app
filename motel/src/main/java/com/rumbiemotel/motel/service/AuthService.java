package com.rumbiemotel.motel.service;


import com.rumbiemotel.motel.entity.Role;
import com.rumbiemotel.motel.entity.User;
import com.rumbiemotel.motel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // By default, new registrations are given the USER role
        user.setRole(Role.USER);
        return userRepository.save(user);
    }
}
