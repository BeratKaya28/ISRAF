package com.israf.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

	private final String UPLOAD_DIR = "C:/israf_uploads/";
	
	public String saveFile(MultipartFile file) throws IOException{
		Path uploadPath = Paths.get(UPLOAD_DIR);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path filePath = uploadPath.resolve(fileName);
		Files.copy(file.getInputStream(), filePath);
		return fileName;	
	}
	
	
	
}
