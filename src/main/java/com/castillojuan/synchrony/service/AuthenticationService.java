package com.castillojuan.synchrony.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.repository.UserRepository;

@Service
public class AuthenticationService {
	

 	private final UserRepository userRepository;

 	@Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
