package com.castillojuan.synchrony.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.castillojuan.synchrony.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

	Image findByImageHash(String imageHash);
	List<Image> findByUserId(Long userId);

}
