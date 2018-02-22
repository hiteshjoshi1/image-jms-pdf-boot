package com.consensys.hitesh.producer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.consensys.hitesh.producer.model.User;
import com.consensys.hitesh.producer.repository.UserRepository;

@Component
public class UserSignUpService {
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

 
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
	
}
