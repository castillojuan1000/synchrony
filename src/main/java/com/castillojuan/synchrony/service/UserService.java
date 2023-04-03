package com.castillojuan.synchrony.service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.castillojuan.synchrony.entity.Image;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.exception.UnauthorizedAccessException;
import com.castillojuan.synchrony.exception.UserNotFoundException;
import com.castillojuan.synchrony.repository.ImageRepository;
import com.castillojuan.synchrony.repository.UserRepository;
import com.castillojuan.synchrony.utils.DecryptToken;

@Service
public class UserService {

	private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public UserService(UserRepository userRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
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
    
    
    public User getUserWithImages(String authHeader, Long userId) {
    	//check authorization 
    	String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    	
    	if(token != null && !authHeader.isBlank()) {
    		//check if user exists
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
    		
    	}else {
    		throw new UnauthorizedAccessException("Unauthorized access");
    	}	
    	
    }
    
    
}
