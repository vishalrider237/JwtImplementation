package com.jwtuthentication.service;

import com.jwtuthentication.entity.User;
import com.jwtuthentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
   @Autowired
   private PasswordEncoder encoder;
    public List<User> getUsers(){

        return this.userRepository.findAll();
    }
    public User createUser(User user){
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(encoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }
}
