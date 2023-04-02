package com.castillojuan.synchrony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.castillojuan.synchrony.service.ImgurService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/imgur")
public class ImgurController {

    @Autowired
    private ImgurService imgurService;

    //upload imgur images 
    @PostMapping("/image")
    public ResponseEntity<JsonNode> uploadImage(@RequestParam("file") MultipartFile image) {
        try {
            byte[] imageData = image.getBytes();
            JsonNode response = imgurService.uploadImage(imageData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    //get imgur images
    @GetMapping("/account/images")
    public ResponseEntity<JsonNode> getAllAccountImages() {
        try {
            JsonNode response = imgurService.getAllAccountImages();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    //delete image
    @DeleteMapping("/image/{imageHash}")
    public ResponseEntity<JsonNode> deleteImage(@PathVariable String imageHash) {
        try {
            JsonNode response = imgurService.deleteImage(imageHash);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}