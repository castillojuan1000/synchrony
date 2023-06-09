package com.castillojuan.synchrony.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.management.loading.PrivateClassLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.castillojuan.synchrony.entity.Image;
import com.castillojuan.synchrony.entity.KafkaImageData;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.exception.ImageNotFoundException;
import com.castillojuan.synchrony.exception.UserNotFoundException;
import com.castillojuan.synchrony.repository.ImageRepository;
import com.castillojuan.synchrony.repository.UserRepository;
import com.castillojuan.synchrony.security.ImgurConfiguration;
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
	

    @Autowired
    private ImgurConfiguration imgurConfig;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private KafkaTemplate<String, KafkaImageData> kafkaTemplate;
    
    
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
    	
    	if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("Image data is not present");
        }
    	
    	//check authorization 
    	String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    	

		String userName =  jwtService.extractUsername(token);
		User user = userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException("User not found"));
		
 
		
		//forming and executing external endpoint (Imgur)
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.png",
                        RequestBody.create(imageData, MediaType.parse("image/png")))
                .build();

        Request request = new Request.Builder()
                .url(imgurConfig.apiUrl + "/image")
                .addHeader("Authorization", "Bearer "+ imgurConfig.auth)
                .post(requestBody)
                .build();
        
        //post image to Imgur user account
        try (Response response = client.newCall(request).execute()) {
        	String responseBody = response.body().string();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            String imageHash = jsonResponse.get("data").get("id").asText();
            String imageLink = jsonResponse.get("data").get("link").asText();   
            
            // create ImageData object
            KafkaImageData kafkaImageData = new KafkaImageData();
            kafkaImageData.setUserName(userName);
            kafkaImageData.setImageLink(imageLink);
            

            // send ImageData to kafka topic
            kafkaTemplate.send("synchrony_topic", kafkaImageData);
	   	 	
  
            //post image to MySQL 	
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
    	String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

		
		String userName =  jwtService.extractUsername(token);
		Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(userName).orElseThrow(() -> new NoSuchElementException("User not found")));
		
		if(optionalUser.isPresent()) {
			
			Image image = imageRepository.findByImageHash(imageHash);
			
	    	//forming and executing Imgur endpoint
	        OkHttpClient client = new OkHttpClient();
	        Request request = new Request.Builder()
	                .url(imgurConfig.apiUrl + "/image/" + imageHash)
	                .addHeader("Authorization", "Bearer "+ imgurConfig.auth)
	                .delete()
	                .build();

	        try (Response response = client.newCall(request).execute()) {
	        	String responseBody = response.body().string();
	            objectMapper.readTree(responseBody);
	            
	            if(image != null) {
	            	 imageRepository.delete(image);
	            }else {
	            	throw new ImageNotFoundException("Image not found with imageHash: " + imageHash);
	            }
	        }
	        
		}
	
    	
    	 
    }
    
}
