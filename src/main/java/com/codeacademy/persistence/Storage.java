package com.codeacademy.persistence;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class Storage {

	
	private static Storage instance;
		
	
	private Storage() {
		
	}
	
	public static Storage getInstance() {
		if(instance == null) {
			instance = new Storage();
		}
		return instance;
	};
	
	
	public void saveFile(String homePath,long id, MultipartFile file) throws IOException {
		FileUploadUtil.saveFile(homePath,id+".png" , file);
	}	
	
	public boolean deleteFile(String homePath,long id) throws IOException {
		return FileUploadUtil.deleteFile(homePath,id+".png");
	}	
	
}
