package com.castillojuan.synchrony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.castillojuan.synchrony.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findUserByEmail(String email);
	Optional<User> findByUsernameAndPassword(String username, String password);
	Optional<User> findByUsername(String username);
}
