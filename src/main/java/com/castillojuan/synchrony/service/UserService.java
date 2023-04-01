package com.castillojuan.synchrony.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        //todo: add password encryption and confirm password and validate email

        // save the user to the database
        return userRepository.save(user);
    }
}
