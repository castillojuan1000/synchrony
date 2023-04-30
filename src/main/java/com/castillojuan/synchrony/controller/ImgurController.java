package com.castillojuan.synchrony.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.castillojuan.synchrony.entity.Image;
import com.castillojuan.synchrony.exception.MissingImageHashException;
import com.castillojuan.synchrony.service.ImgurService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/imgur")
public class ImgurController {

    @Autowired
    private ImgurService imgurService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * The uploadImage controller method is an endpoint 
     * that handles image uploading for authenticated users. 
     * @param authHeader
     * @param image
     * @return
     * @throws IOException 
     */
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestHeader("Authorization") String authHeader,@RequestParam("image") MultipartFile image) throws IOException {

            byte[] imageData = image.getBytes();
            Image response = imgurService.uploadImage(imageData,authHeader);
            
            return ResponseEntity.ok(response);	
    }
    
    
    /**
     * The deleteImage controller method is an endpoint 
     * that handles the deletion of an image for authenticated users.
     * @param authHeader
     * @param imageHash
     * @return
     * @throws IOException 
     */
    @DeleteMapping("/image/{imageHash}")
    public ResponseEntity<?> deleteImage(@RequestHeader("Authorization") String authHeader, @PathVariable String imageHash) throws IOException {
    	
		imgurService.deleteImage(imageHash, authHeader);
		Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Image with imageHash " + imageHash + " has been deleted.");

		return ResponseEntity.ok(response);

	}
    
    @DeleteMapping({"/image", "/image/"})
    public ResponseEntity<?> handleMissingImageHash() {
    	throw new MissingImageHashException("An image id is required. Please include an image id in the request path.");
    }
}