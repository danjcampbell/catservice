package com.codeacademy.persistence;

import java.io.*;
import java.nio.file.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

	// We are saving the Images directly in the file system
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}

	public static boolean deleteFile(String uploadDir, String fileName) {
		try {
			Path uploadPath = Paths.get(uploadDir);
			Path filePath = uploadPath.resolve(fileName);
			return filePath.toFile().delete();
		} catch (Exception e) {
			LOGGER.error(e.toString(),e);
			return false;
		}

	}
}