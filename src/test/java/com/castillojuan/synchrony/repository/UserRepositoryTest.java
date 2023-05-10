package com.castillojuan.synchrony.repository;




import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.castillojuan.synchrony.entity.User;
import com.castillojuan.synchrony.enums.Role;



@DataJpaTest
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @AfterEach
    void tearDown() {
    	userRepository.deleteAll();
    }
    
    @Test
    public void testFindUserByEmail() {
        // Create a new user with a unique email
        User user = User.builder()
            .username("testuser")
            .password("password")
            .firstName("John")
            .lastName("Doe")
            .email("test@example.com")
            .role(Role.USER)
            .build();
        userRepository.save(user);
        
        // Test that the user can be found by their email
        Optional<User> foundUser = userRepository.findUserByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
        
        // Test that a non-existent user cannot be found by their email
        Optional<User> notFoundUser = userRepository.findUserByEmail("notfound@example.com");
        assertFalse(notFoundUser.isPresent());
    }
}
