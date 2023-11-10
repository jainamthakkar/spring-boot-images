package com.imageUploading.service;

import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import jakarta.persistence.criteria.Path;

public interface FilesStorageService {
	  public void init();

	  public void save(MultipartFile file);

	  public Resource load(String filename);

//	  public boolean delete(String filename);
	  
//	  public void deleteAll();

	  public Stream<java.nio.file.Path> loadAll();
	}
