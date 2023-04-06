package com.castillojuan.synchrony.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.exception.UnauthorizedAccessException;
import com.castillojuan.synchrony.service.UserService;
import com.castillojuan.synchrony.utils.Logs;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
public class UserController {
//	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	Logger logger = Logs.getLogger();
	
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The createUser controller method is an HTTP POST endpoint 
     * that creates and saves a new User object in the system. 
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
    	logger.info("Creating user");
        User createdUser = userService.createUser(user);
        
        logger.info("Finished creating user");
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        
    }
    
    /**
     * The getUserWithImages controller method is an endpoint 
     * that retrieves a User object along with its associated Image objects.
     * @param authHeader
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserWithImages(@RequestHeader("Authorization") String authHeader, @PathVariable Long userId) {
    	logger.info("Getting user info and images");
        try {
        	User user = userService.getUserWithImages(authHeader, userId);
        	logger.info("Finished getting user info and images");
            return ResponseEntity.ok(user);
        }catch(NoSuchElementException e) {
        	logger.warning("User not found.");
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found.");
        }catch(UnauthorizedAccessException e) {
        	logger.warning("Unauthorized access.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");

        }
    	
    }
    
}
