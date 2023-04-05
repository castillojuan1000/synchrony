package com.castillojuan.synchrony.service;

import java.io.IOException;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.castillojuan.synchrony.ImgurConfiguration;
import com.castillojuan.synchrony.controller.UserController;
import com.castillojuan.synchrony.entity.Image;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.exception.UnauthorizedAccessException;
import com.castillojuan.synchrony.repository.ImageRepository;
import com.castillojuan.synchrony.repository.UserRepository;
import com.castillojuan.synchrony.utils.DecryptToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class ImgurService implements Serializable{
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private ImgurConfiguration imgurConfig;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    
   /**
    * This service method is responsible for uploading an image to an external image hosting service (Imgur) 
    * and storing its metadata in a local in-memory database.
    * @param imageData
    * @param authHeader
    * @return
    * @throws IOException
    */
    public Image uploadImage(byte[] imageData, String authHeader) throws IOException {
    	logger.info("Upload Image Started.");

    	//check authorization 
    	String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    	
    	if(token == null || authHeader.isBlank()) {
    		logger.error("Unauthorized access.");
    		throw new UnauthorizedAccessException("Unauthorized access.");
    	}
    	

		String userName =  DecryptToken.decryptToken(token);
		User user = userRepository.findByUsername(userName).orElseThrow(() -> new NoSuchElementException("User not found"));
		
		//forming and executing external endpoint (Imgur)
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.png",
                        RequestBody.create(imageData, MediaType.parse("image/png")))
                .build();

        Request request = new Request.Builder()
                .url(imgurConfig.apiUrl + "/image")
                .addHeader("Authorization", "Bearer 5edaed792b4f552fb9f4b03356a3e7ef8c3d7337")
                .post(requestBody)
                .build();
        
        //post image to Imgur user account
        try (Response response = client.newCall(request).execute()) {
        	String responseBody = response.body().string();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            String imageHash = jsonResponse.get("data").get("id").asText();
            String imageLink = jsonResponse.get("data").get("link").asText();   
  
            //post image to H2 	
	   	 	Image image = new Image(imageHash, imageLink, user);
	        return imageRepository.save(image);
	                    
				
        }
        
	
  	
    }
    
    
    
    
    /**
     * This service method is responsible for deleting an image from an external image hosting service (Imgur) 
     * and removing its metadata from a local in-memory database.
     * @param imageHash
     * @param authHeader
     * @throws IOException
     */
    public void deleteImage(String imageHash,String authHeader) throws IOException {
    	logger.info("Delete Image started.");
    	//Authorization
    	String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;


    	if(token == null || authHeader.isBlank()) {
    		logger.error("Unauthorized access.");
    		throw new UnauthorizedAccessException("Unauthorized access");
    	}
    	

		
		String userName =  DecryptToken.decryptToken(token);
		Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(userName).orElseThrow(() -> new NoSuchElementException("User not found")));
		
		if(optionalUser.isPresent()) {
			
			Image image = imageRepository.findByImageHash(imageHash);
			
	    	//forming and executing Imgur endpoint
	        OkHttpClient client = new OkHttpClient();
	        Request request = new Request.Builder()
	                .url(imgurConfig.apiUrl + "/image/" + imageHash)
	                .addHeader("Authorization", "Bearer 5edaed792b4f552fb9f4b03356a3e7ef8c3d7337")
	                .delete()
	                .build();

	        try (Response response = client.newCall(request).execute()) {
	        	String responseBody = response.body().string();
	            objectMapper.readTree(responseBody);
	            
	            if(image != null) {
	            	 imageRepository.delete(image);
	            }else {
	            	logger.error("Image not found with imageHash: " + imageHash);
	            	throw new IllegalStateException("Image not found with imageHash: " + imageHash);
	            }
	        }
	        
		}else {
			logger.error("User not found");
			throw new NoSuchElementException("User not found");
		}
	
    	
    	 
    }
    
}
