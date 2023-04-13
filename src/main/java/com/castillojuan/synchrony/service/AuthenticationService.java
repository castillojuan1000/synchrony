package com.castillojuan.synchrony.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.repository.UserRepository;
import com.castillojuan.synchrony.security.AuthenticationResponse;

import lombok.experimental.var;

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

    public AuthenticationResponse authenticate(String username, String password) {
    	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    	
    	Optional<User> user = userRepository.findByUsername(username);
		UserDetails userDetails = user.get();
    	
		String jwtToken = jwtService.generateToken(userDetails);
        
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
