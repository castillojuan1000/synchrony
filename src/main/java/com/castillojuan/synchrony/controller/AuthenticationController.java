package com.castillojuan.synchrony.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.castillojuan.synchrony.entity.Auth;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.service.AuthenticationService;
import com.castillojuan.synchrony.utils.GenerateToken;

import com.castillojuan.synchrony.utils.Logs;
import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	Logger logger = Logs.getLogger();

	private final AuthenticationService authenticationService;

	@Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Auth auth) {
    	logger.info("Authenticating user.");
    	String username = auth.getUsername();
        String password = auth.getPassword();
        
        Optional<User> user = authenticationService.authenticate(username, password);
        if (user.isPresent()) {
            String token = GenerateToken.generateToken(username);
            logger.info("Finished authenticating user.");
            return ResponseEntity.ok(token);
        } else {
        	logger.warning("Invalid username or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
