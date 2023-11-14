package com.imageUploading.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.imageUploading.model.Images;
import com.imageUploading.repository.uploadRepository;
import com.imageUploading.service.FilesStorageService;

import ch.qos.logback.classic.Logger;
import org.springframework.core.io.Resource;

@Controller
public class HomeController {

	@Autowired
	FilesStorageService storageService;

	@Autowired
	private uploadRepository uploadRepo;
	
//	private static final org.slf4j.Logger log = LoggerFactory.getLogger(HomeController.class);


	@GetMapping("/")
	public String getListFiles(Model model) {

		try {

			List<Images> fileInfos = storageService.loadAll().map(path -> {
				String filename = path.getFileName().toString();
				String url = MvcUriComponentsBuilder
					    .fromMethodName(HomeController.class, "getFile", path.getFileName().toString())
					    .build().toString();
//				log.info("Generated URL: {}", url);
				return new Images(filename, url);
			}).collect(Collectors.toList());

			model.addAttribute("files", fileInfos);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "index";
	}

//	@GetMapping("/files")
//	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//		Resource file = storageService.load(filename);
//
//		return ResponseEntity.ofNullable(null);
////	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file)
//	}
	
	@GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

	@PostMapping("/imageUpload")
	public String uploadFile(Model model, @RequestParam("img") MultipartFile file) {
		String message = "";

		try {
			System.out.println("inside /imageUpload");
			storageService.save(file);

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			model.addAttribute("message", message);

			Images im = new Images();
			im.setImageName(file.getOriginalFilename());
			Images uploadImg = uploadRepo.save(im);

			System.out.println("uploaded");
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			model.addAttribute("message", message);
			e.printStackTrace();
		}  

		return "redirect:/";
	}

}

//import java.io.InputStream;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.core.io.Resource;
//
//import com.imageUploading.model.Images;
//import com.imageUploading.repository.uploadRepository;
//
//import jakarta.servlet.http.HttpSession;
//
//@Controller
//public class HomeController {
//
//    @Autowired
//    private uploadRepository uploadRepo;
//
//    @Autowired
//    private ResourceLoader resourceLoader;
//
//    @GetMapping("/")
//    public String index(Model m) {
//        List<Images> list = uploadRepo.findAll();
//        m.addAttribute("list", list);
//        return "index";
//    }
//
//    @PostMapping("/imageUpload")
//    public String imageUpload(@RequestParam MultipartFile img, HttpSession session) {
//        try {
//            // Load the classpath resource using ResourceLoader
//            Resource resource = resourceLoader.getResource("classpath:static/img/" + img.getOriginalFilename());
//
//            // Get an InputStream from the resource
//            InputStream inputStream = resource.getInputStream();
//
//            // Create and save an Images object
//            Images im = new Images();
//            im.setImageName(img.getOriginalFilename());
//            Images uploadImg = uploadRepo.save(im);
//
//            if (uploadImg != null) {
//                session.setAttribute("msg", "Image Upload Successfully");
//            } else {
//                session.setAttribute("msg", "Failed to save image information");
//            }
//
//            // Use the InputStream to read or write data as needed
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.setAttribute("msg", "Image Upload Failed");
//        }
//        return "redirect:/";
//    }
//}

//import java.io.File;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.imageUploading.model.Images;
//import com.imageUploading.repository.uploadRepository;
//
//import jakarta.servlet.http.HttpSession;
//
//@Controller
//public class HomeController {
//
//	@Autowired
//	private uploadRepository uploadRepo;
//
//	@GetMapping("/")
//	public String index(Model m) {
//
//		List<Images> list = uploadRepo.findAll();
//
//		m.addAttribute("list", list);
//
//		return "index";
//	}

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

//	@PostMapping("/imageUpload")
//	public String imageUpload(@RequestParam MultipartFile img, HttpSession session) {
//		
//		String data = "";
//		ClassPathResource resource = new ClassPathResource("/static/img");
//		
//		try {
//			byte[] dataArr = FileCopyUtils.copyToByteArray(resource.getInputStream());
//			Path path = Paths.get(resource.getPath() + File.separator + img.getOriginalFilename());
//		    data = new String(dataArr, StandardCharsets.UTF_8);
//		    System.out.println(path + "\n" + data);
//		    Files.write(path, img.getBytes());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "redirect:/";
//	}

//	@PostMapping("/imageUpload")
//	public String imageUpload(@RequestParam MultipartFile img, HttpSession session) {
//	    try {
//	    	File saveFile = new ClassPathResource("/static/img").getFile();
//	        // Save the uploaded image to the "static/img" directory
//	        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
////	        Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
////	        Path pathForWriting = Paths.get("src/main/resources/static/img/" + img.getOriginalFilename());
//	        Files.write(path, img.getBytes());
//	        
//	        // Create and save an Images object
//	        Images im = new Images();
//	        im.setImageName(img.getOriginalFilename());
//	        Images uploadImg = uploadRepo.save(im);
//
//	        if (uploadImg != null) {
//	            session.setAttribute("msg", "Image Upload Successfully");
//	        } else {
//	            session.setAttribute("msg", "Failed to save image information");
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        session.setAttribute("msg", "Image Upload Failed");
//	    }
//	    return "redirect:/";
//	}

//}
