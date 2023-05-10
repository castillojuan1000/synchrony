package com.castillojuan.synchrony.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.castillojuan.synchrony.dto.UserDTO;
import com.castillojuan.synchrony.entity.Image;
import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.exception.InvalidJwtException;
import com.castillojuan.synchrony.exception.UserNotFoundException;
import com.castillojuan.synchrony.repository.ImageRepository;
import com.castillojuan.synchrony.repository.UserRepository;

class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private ImageRepository imageRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	private AutoCloseable autoCloseable;
	private UserService userServiceUnderTest;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		userServiceUnderTest = new UserService(userRepository, imageRepository, passwordEncoder, jwtService);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

//	@Test
//	@Disabled
//	void testCreateUser() {
//		fail("Not yet implemented");
//	}

	@Test
	void testGetUserWithImages() {
		// Mock the behavior of the jwtService to return a known username
		when(jwtService.extractUsername("token")).thenReturn("testuser");

		// Mock the behavior of the userRepository to return a known user with ID 1
		User user = new User();
		user.setId(1L);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// Mock the behavior of the imageRepository to return a list of known images
		Image image1 = new Image();
		Image image2 = new Image();
		when(imageRepository.findByUserId(1L)).thenReturn(Arrays.asList(image1, image2));

		// Call the getUserWithImages method and verify that it returns the expected
		// UserDTO object
		UserDTO result = userServiceUnderTest.getUserWithImages("Bearer token", 1L);

		assertEquals(1L, result.getId());
		assertEquals(2, result.getImages().size());

		// Verify that the userRepository and imageRepository were called with the
		// expected arguments
		verify(userRepository).findByUsername("testuser");
		verify(userRepository).findById(1L);
		verify(imageRepository).findByUserId(1L);
	}

//	@Test
//	void testGetUserWithImages_userNotFound() {
//		// Mock the behavior of the jwtService to return a known username
//		when(jwtService.extractUsername("token")).thenReturn("testuser");
//
//		// Mock the behavior of the userRepository to return an empty optional
//		when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
//
//		// Call the getUserWithImages method and verify that it throws a
//		// UserNotFoundException
//		assertThrows(UserNotFoundException.class, () -> userServiceUnderTest.getUserWithImages("Bearer token", 1L));
//
//		// Verify that the userRepository was called with the expected argument
//		verify(userRepository).findByUsername("testuser");
//	}

	@Test
	void testGetUserWithImages_imageNotFound() {
		// Mock the behavior of the jwtService to return a known username
		when(jwtService.extractUsername("token")).thenReturn("testuser");

		// Mock the behavior of the userRepository to return a known user with ID 1
		User user = new User();
		user.setId(1L);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// Mock the behavior of the imageRepository to return an empty list
		when(imageRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

		// Call the getUserWithImages method and verify that it returns the expected
		// UserDTO object
		UserDTO result = userServiceUnderTest.getUserWithImages("Bearer token", 1L);
		assertEquals(1L, result.getId());
		assertEquals(0, result.getImages().size());

		// Verify that the userRepository and imageRepository were called with the
		// expected arguments
		verify(userRepository).findByUsername("testuser");
		verify(userRepository).findById(1L);
		verify(imageRepository).findByUserId(1L);
	}

	@Test
	void testGetUserWithImages_invalidToken() {
		// Mock the behavior of the jwtService to throw an InvalidTokenException
		when(jwtService.extractUsername("invalid")).thenThrow(new InvalidJwtException("Invalid token"));

		// Call the getUserWithImages method and verify that it throws an
		// InvalidTokenException
		assertThrows(InvalidJwtException.class, () -> userServiceUnderTest.getUserWithImages("Bearer invalid", 1L));

		// Verify that the jwtService was called with the expected argument
		verify(jwtService).extractUsername("invalid");
	}

}
