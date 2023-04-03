package com.castillojuan.synchrony.controller;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.castillojuan.synchrony.entity.Image;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.repository.UserRepository;
import com.castillojuan.synchrony.service.ImgurService;
import com.castillojuan.synchrony.utils.DecryptToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/imgur")
public class ImgurController {

    @Autowired
    private ImgurService imgurService;
    @Autowired
    private UserRepository userRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    //upload imgur images 
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestHeader("Authorization") String authHeader,@RequestParam("image") MultipartFile image) {
    	
    	String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    	
    	if(token != null && !authHeader.isBlank()) {
    		String userName =  DecryptToken.decryptToken(token);
    		User user = userRepository.findByUsername(userName).orElseThrow(() -> new NoSuchElementException("User not found"));
    		
    		try {
                byte[] imageData = image.getBytes();
                Image response = imgurService.uploadImage(imageData, user.getId());
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
    		
    	}else {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
    	}
    	
    }
    
    
    //delete image
    @DeleteMapping("/image/{imageHash}")
    public ResponseEntity<?> deleteImage(@RequestHeader("Authorization") String authHeader, @PathVariable String imageHash) {
    	
    	String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    	
    	if(token != null && !authHeader.isBlank()) {
    		
    		String userName =  DecryptToken.decryptToken(token);
//    		User user = userRepository.findByUsername(userName).orElseThrow(() -> new NoSuchElementException("User not found"));
    		
    		Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(userName).orElseThrow(() -> new NoSuchElementException("User not found")));
    		
    		if(optionalUser.isPresent()) {
    			try {
    	            imgurService.deleteImage(imageHash);
    	            JsonNode response = objectMapper.createObjectNode().put("message", "Image with imageHash " + imageHash + " has been deleted.");
    	            return ResponseEntity.ok(response);
    	        } catch (Exception e) {
    	        	return ResponseEntity.status(500).build();
    	        }
    		}else{
    			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found.");
    		}
    		
    		 
    		
    	}else {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
    	}


    }
}