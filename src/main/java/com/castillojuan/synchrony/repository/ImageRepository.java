package com.castillojuan.synchrony.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.castillojuan.synchrony.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
