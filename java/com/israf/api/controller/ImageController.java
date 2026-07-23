package com.israf.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.israf.api.service.FileService;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins="*")
public class ImageController {

	private final FileService fileService;
	
	public ImageController(FileService fileService) {
		this.fileService = fileService;
	}
	
	@PostMapping("/upload-multiple")
	public ResponseEntity<List<String>> uploadImage(@RequestParam("files") MultipartFile[] files){
		List<String> filesUrls = new ArrayList<>();
		for(MultipartFile f : files) {
			try {
				String fileName = fileService.saveFile(f);
				String fileUrl = "http://localhost:8080/uploads/" + fileName;
	            filesUrls.add(fileUrl);
			}catch(IOException e) {
				System.err.println("Dosya yüklenemedi: " + f.getOriginalFilename());
			}
		}
		
		return ResponseEntity.ok(filesUrls);
	}
	
	@PostMapping("/upload-multiple-customer")
	public ResponseEntity<List<String>> uploadImageCustomer(@RequestParam("files") MultipartFile[] files){
		List<String> filesUrls = new ArrayList<>();
		
		for(MultipartFile f : files) {
			try {
				String fileName = fileService.saveFile(f);
				String fileUrl = "http://192.168.1.103:8080/uploads/" + fileName;
	            filesUrls.add(fileUrl);
			}catch(IOException e) {
				System.err.println("Dosya yüklenemedi: " + f.getOriginalFilename());
			}
		}
		
		return ResponseEntity.ok(filesUrls);
	}
	
	
	
}
