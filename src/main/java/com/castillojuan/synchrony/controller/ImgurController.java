package com.castillojuan.synchrony.controller;

import java.util.NoSuchElementException;

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
import com.castillojuan.synchrony.exception.UnauthorizedAccessException;
import com.castillojuan.synchrony.service.ImgurService;
import com.fasterxml.jackson.databind.JsonNode;
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
     */
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestHeader("Authorization") String authHeader,@RequestParam("image") MultipartFile image) {
    	
    	try {
            byte[] imageData = image.getBytes();
            Image response = imgurService.uploadImage(imageData,authHeader);
            return ResponseEntity.ok(response);
        }catch(UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    	
    }
    
    
    /**
     * The deleteImage controller method is an endpoint 
     * that handles the deletion of an image for authenticated users.
     * @param authHeader
     * @param imageHash
     * @return
     */
    @DeleteMapping("/image/{imageHash}")
    public ResponseEntity<?> deleteImage(@RequestHeader("Authorization") String authHeader, @PathVariable String imageHash) {
    	
    	try {
            imgurService.deleteImage(imageHash, authHeader);
            JsonNode response = objectMapper.createObjectNode().put("message", "Image with imageHash " + imageHash + " has been deleted.");
            return ResponseEntity.ok(response);
        }catch(UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");

        }catch(NoSuchElementException e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found.");
        }catch(IllegalStateException e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}