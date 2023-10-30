package com.imageUploading.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.imageUploading.model.Images;
import com.imageUploading.repository.uploadRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private uploadRepository uploadRepo;

	@GetMapping("/")
	public String index(Model m) {

		List<Images> list = uploadRepo.findAll();

		m.addAttribute("list", list);

		return "index";
	}

//	@PostMapping("/imageUpload")
//	public String imageUpload(@RequestParam MultipartFile img, HttpSession session) {
//		
//			try {
//				
//				Images im = new Images();
//				im.setImageName(img.getOriginalFilename());
//
//				Images uploadImg = uploadRepo.save(im);
//
//				File saveFile = new ClassPathResource("/static/img").getFile();
//
//				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
// 				System.out.println(path);
////				Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
// 				Files.write(path, img.getBytes());
//				
//				System.out.println(img.getInputStream() + " " + path + "  " + "uploaded");
//				
//				 // Create and save an Images object
////		        Images im = new Images();
////		        im.setImageName(img.getOriginalFilename());
////		        Images uploadImg = uploadRepo.save(im);
//	
//				if (uploadImg != null) {
//		            session.setAttribute("msg", "Image Upload Successfully");
//		        } else {
//		            session.setAttribute("msg", "Failed to save image information");
//		        }
//
////				StringBuilder fileNames = new StringBuilder();
////				Path fileNameAndPath = Paths.get("src/main/resources/static/img/" + img.getOriginalFilename());
////				fileNames.append(img.getOriginalFilename());
////				Files.write(fileNameAndPath, img.getBytes());
//////				model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
////				System.out.println(fileNameAndPath + "  " + "uploaded");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//	
//		return "redirect:/";
//	}

	@PostMapping("/imageUpload")
	public String imageUpload(@RequestParam MultipartFile img, HttpSession session) {
	    try {
	    	File saveFile = new ClassPathResource("/static/img").getFile();
	        // Save the uploaded image to the "static/img" directory
	        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
//	        Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//	        Path pathForWriting = Paths.get("src/main/resources/static/img/" + img.getOriginalFilename());
	        Files.write(path, img.getBytes());
	        
	        // Create and save an Images object
	        Images im = new Images();
	        im.setImageName(img.getOriginalFilename());
	        Images uploadImg = uploadRepo.save(im);

	        if (uploadImg != null) {
	            session.setAttribute("msg", "Image Upload Successfully");
	        } else {
	            session.setAttribute("msg", "Failed to save image information");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.setAttribute("msg", "Image Upload Failed");
	    }
	    return "redirect:/";
	}

}
