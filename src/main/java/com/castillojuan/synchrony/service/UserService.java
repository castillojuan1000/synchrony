package com.castillojuan.synchrony.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.exception.UserNotFoundException;
import com.castillojuan.synchrony.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    public User createUser(User user) {
        //todo:  add password encryption and confirm password and validate email
    	
    	Optional<User> userByEmailOptional = userRepository.findUserByEmail(user.getEmail());
    	if(userByEmailOptional.isPresent()) {
    		throw new IllegalStateException("A user with the same email already exists");
    	}

        // save the user to the database
        return userRepository.save(user);
    }
    
    public User getUserById(Long userId) {
    	//todo: authorizing user and get all the details including images
    	
       Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }
}
