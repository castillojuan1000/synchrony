package com.castillojuan.synchrony.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.castillojuan.synchrony.dto.UserDTO;
import com.castillojuan.synchrony.entity.AuthResquest;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.security.AuthenticationResponse;
import com.castillojuan.synchrony.service.AuthenticationService;
import com.castillojuan.synchrony.service.UserService;
import com.castillojuan.synchrony.utils.GenerateToken;

import com.castillojuan.synchrony.utils.Logs;
import com.castillojuan.synchrony.utils.Util;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final UserService userService;


	@Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }
	
	 /**
     * The createUser controller method is an HTTP POST endpoint 
     * that creates and saves a new User object in the system. 
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
    	
        return ResponseEntity.ok(userService.createUser(user));
        
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthResquest auth) {
    	String username = auth.getUsername();
        String password = auth.getPassword();
        
        return ResponseEntity.ok(authenticationService.authenticate(username, password));
    }
}
