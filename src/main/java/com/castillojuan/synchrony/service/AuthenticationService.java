package com.castillojuan.synchrony.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.exception.UserNotFoundException;
import com.castillojuan.synchrony.repository.UserRepository;
import com.castillojuan.synchrony.security.AuthenticationResponse;

@Service
public class AuthenticationService {
	

 	private final UserRepository userRepository;
 	private final PasswordEncoder passwordEncoder;
 	private final AuthenticationManager authenticationManager;
 	private final JwtService jwtService;

 	@Autowired
    public AuthenticationService(
    		UserRepository userRepository, 
    		PasswordEncoder passwordEncoder,
    		AuthenticationManager authenticationManager,
    		JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

 	/*
 	 * The authenticationManager.authenticate() method is called with a new UsernamePasswordAuthenticationToken 
 	 * object created using the provided username and password. 
 	 * This step attempts to authenticate the user by checking the provided credentials against the stored user information. 
 	 * If the authentication fails, an exception will be thrown.
 	 */
    public AuthenticationResponse authenticate(String username, String password) {
    	
    	try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("User not found or invalid credentials");
        }
    	
    	Optional<User> user = userRepository.findByUsername(username);
		UserDetails userDetails = user.get();
    	
		String jwtToken = jwtService.generateToken(userDetails);
        
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
