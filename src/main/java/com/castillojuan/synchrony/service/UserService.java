package com.castillojuan.synchrony.service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.castillojuan.synchrony.controller.UserController;
import com.castillojuan.synchrony.entity.Image;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.exception.UnauthorizedAccessException;
import com.castillojuan.synchrony.repository.ImageRepository;
import com.castillojuan.synchrony.repository.UserRepository;
import com.castillojuan.synchrony.utils.DecryptToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
	

	private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ImageRepository imageRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * The createUser method is a service method that creates and saves a new User object in the database. 
     * It takes a User object as a parameter.
     * @param user
     * @return
     */
    public User createUser(User user) {
    	Optional<User> userByEmailOptional = userRepository.findUserByEmail(user.getEmail());
    	if(userByEmailOptional.isPresent()) {
    		throw new IllegalStateException("A user with the same email already exists");
    	}

    	
    	// Hash the user's password and set it on the user object
    	String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        
     // save the user to the database
        return userRepository.save(user);
    }
    
    /**
     * getUserWithImages, is a service method that retrieves a User object along with its associated Image objects. 
     * The method takes two parameters as input: a String authHeader and a Long userId.
     * @param authHeader
     * @param userId
     * @return
     */
    public User getUserWithImages(String authHeader, Long userId) {
    	//check authorization 
    	String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    	
    	if(token == null || authHeader.isBlank()) {
    		throw new UnauthorizedAccessException("Unauthorized access");
    	}
    	

		//check if user exists and get user info 
		String userName =  DecryptToken.decryptToken(token);
		Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(userName).orElseThrow(() -> new NoSuchElementException("User not found")));
		
		if(optionalUser.isPresent()) {
			User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
	        List<Image> images = imageRepository.findByUserId(userId);
	        user.setImages(images);
	        return user;
		}else {
			throw new NoSuchElementException("User not found");
		}
		
	
    	
    }
    
    
}
