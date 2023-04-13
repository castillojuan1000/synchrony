package com.castillojuan.synchrony.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.castillojuan.synchrony.dto.UserDTO;
import com.castillojuan.synchrony.exception.UnauthorizedAccessException;
import com.castillojuan.synchrony.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    	
        try {
        	UserDTO user = userService.getUserWithImages(authHeader, userId);
        	
            return ResponseEntity.ok(user);
        }catch(NoSuchElementException e) {
        	
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found.");
        }catch(UnauthorizedAccessException e) {
        	
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");

        }
    	
    }
    
}
