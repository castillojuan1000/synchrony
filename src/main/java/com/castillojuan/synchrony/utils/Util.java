package com.castillojuan.synchrony.utils;

import com.castillojuan.synchrony.dto.UserDTO;
import com.castillojuan.synchrony.entity.User;

public class Util {

	public static  UserDTO toUserResponseDTO(User user) {
	    UserDTO dto = new UserDTO();
	    
	    dto.setId(user.getId());
	    dto.setFirstName(user.getFirstName());
	    dto.setLastName(user.getLastName());
	    dto.setUsername(user.getUsername());
	    dto.setEmail(user.getEmail());
	    
	    // Set any other fields from the User entity to the UserResponseDTO
	    return dto;
	}
}
