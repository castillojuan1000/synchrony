package com.castillojuan.synchrony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.castillojuan.synchrony.service.ImgurService;

@RestController
@RequestMapping("/imgur")
public class ImgurController {

    @Autowired
    private ImgurService imgurService;

    //upload imgur images 
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile image) {
        try {
            byte[] imageData = image.getBytes();
            String response = imgurService.uploadImage(imageData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
    //get imgur images
    @GetMapping("/account/images")
    public ResponseEntity<String> getAllAccountImages() {
        try {
            String response = imgurService.getAllAccountImages();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}