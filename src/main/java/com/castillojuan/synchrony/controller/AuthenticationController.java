package com.castillojuan.synchrony.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.castillojuan.synchrony.entity.AuthResquest;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.service.AuthenticationService;
import com.castillojuan.synchrony.service.UserService;

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
