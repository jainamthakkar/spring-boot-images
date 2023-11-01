package com.imageUploading;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.imageUploading.service.FilesStorageService;

import jakarta.annotation.Resource;

@SpringBootApplication
public class ImageUploadingApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {

		SpringApplication.run(ImageUploadingApplication.class, args);
		System.out.println("hello!!");
	}

	@Override
	public void run(String... arg) throws Exception {
		System.out.println("inside run");
//	    storageService.deleteAll();
		storageService.init();
	}

}
