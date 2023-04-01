package com.castillojuan.synchrony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.castillojuan.synchrony.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	//find student by email
	Optional<User> findUserByEmail(String email);
}
