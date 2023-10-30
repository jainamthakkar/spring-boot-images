package com.imageUploading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imageUploading.model.Images;


public interface uploadRepository extends JpaRepository<Images, Integer> {

} 