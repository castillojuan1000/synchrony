package com.castillojuan.synchrony.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.castillojuan.synchrony.dto.UserDTO;
import com.castillojuan.synchrony.entity.Image;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.enums.Role;
import com.castillojuan.synchrony.exception.UserNotFoundException;
import com.castillojuan.synchrony.repository.ImageRepository;
import com.castillojuan.synchrony.repository.UserRepository;
import com.castillojuan.synchrony.security.AuthenticationResponse;
import com.castillojuan.synchrony.utils.Util;

@Service
public class UserService {
	

	private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, 
    		ImageRepository imageRepository, 
    		PasswordEncoder passwordEncoder,
    		JwtService jwtService) {
    	
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * The createUser method is a service method that creates and saves a new User object in the database. 
     * It takes a User object as a parameter.
     * @param user
     * @return
     */
    public AuthenticationResponse createUser(User user) {
    	Optional<User> userByEmailOptional = userRepository.findUserByEmail(user.getEmail());
    	if(userByEmailOptional.isPresent()) {
    		throw new IllegalStateException("A user with the same email already exists");
    	}
        
        User builtUser = User.builder()
        				.firstName(user.getFirstName())
        				.lastName(user.getLastName())
        				.email(user.getEmail())
        				.username(user.getUsername())
        				.password(passwordEncoder.encode(user.getPassword()))
        				.role(Role.USER)
        				.build();
        
        userRepository.save(builtUser);
        
        String jwtToken = jwtService.generateToken(builtUser);
     // save the user to the database
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
    
    /**
     * getUserWithImages, is a service method that retrieves a User object along with its associated Image objects. 
     * The method takes two parameters as input: a String authHeader and a Long userId.
     * @param authHeader
     * @param userId
     * @return
     */
	public UserDTO getUserWithImages(String authHeader, Long userId) {
		// check authorization
		String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

		// check if user exists and get user info
		String userName = jwtService.extractUsername(token);
		Optional<User> optionalUser = Optional.ofNullable(
				userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException("User not found")));

		if (optionalUser.isPresent()) {
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
			List<Image> images = imageRepository.findByUserId(userId);
			user.setImages(images);

			UserDTO uerDto = Util.toUserResponseDTO(user);
			return uerDto;
		} else {
			throw new UserNotFoundException("User not found");
		}

	}
    
    
}
