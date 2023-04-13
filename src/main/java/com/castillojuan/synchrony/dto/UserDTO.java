package com.castillojuan.synchrony.dto;

import java.util.List;

import com.castillojuan.synchrony.entity.Image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<Image> images;

}

