package com.codeacademy.cats.resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codeacademy.persistence.Storage;

@RestController
@RequestMapping("/catservice/api")
public class CatService {

	private static final String CAT_IMAGES_HOME_PATH = "cat-images";
	private static final Logger LOGGER = LoggerFactory.getLogger(CatService.class);

	// This endpoint will take in a file as a parameter and save the the file, it
	// will
	// return a message with the Id generated for the file
	@PostMapping(value = "/uploadCatPic")
	public ResponseEntity<String> uploadCatImage(@RequestParam("file") MultipartFile file) {
		try {
			LOGGER.info("file: " + file.getOriginalFilename() + ",bytes: " + file.getBytes().toString());

			long fileId = System.currentTimeMillis();
			Storage.getInstance().saveFile(CAT_IMAGES_HOME_PATH + File.separator, fileId, file);
			String message = "Successfully uploaded " + file.getOriginalFilename() + ", ID:" + fileId + ".";
			return ResponseEntity.ok(message);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return ResponseEntity.badRequest().body("Error");
		}
	}

	// This will take an id parameter from the path, it will return the image with
	// the id given
	// if the id is not present it will return a 404 error
	@GetMapping(value = "/retrieveCatPicById/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getCatImage(@PathVariable Long id) {

		try {
			LOGGER.info("Retreiving cat pic wit Id of" + id);

			Path path = Paths.get(CAT_IMAGES_HOME_PATH + File.separator + id + ".png");
			Resource resource = new UrlResource(path.toUri());

			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png"))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(IOUtils.toByteArray(resource.getInputStream()));

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	// This will return a alphanumeric sorted list of the files that have been
	// uploaded
	// There are 2 optional parameters for limit and offset for paging through the
	// list
	@GetMapping(value = "/retrieveCatPicList")
	public ResponseEntity<List<String>> getCatImage(@RequestParam Optional<Integer> limit,
			@RequestParam Optional<Integer> offset) {

		try {
			LOGGER.info("Retreiving cat pic list");

			Path path = Paths.get(CAT_IMAGES_HOME_PATH);
			Resource resource = new UrlResource(path.toUri());

			String[] files = resource.getFile().list();
			List<String> fileList;
			if (files == null || files.length == 0) {
				fileList = Collections.emptyList();

			} else {
				fileList = Arrays.asList(files);

			}
			Collections.sort(fileList);
			Integer offsetValue = 0;
			Integer limitValue = 100;
			if (offset.isPresent()) {
				offsetValue = offset.get() < fileList.size() ? offset.get() : fileList.size();
			}

			if (limit.isPresent()) {
				limitValue = limit.get() + offsetValue;
			}

			limitValue = limitValue > fileList.size() ? fileList.size() : limitValue;

			fileList = fileList.subList(offsetValue, limitValue);

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(fileList);

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	// This will allow the user to update a picture by its id
	// This will replace the existing image or create a new image if the id was not
	// present
	// with the id that is given in the parameter
	@PutMapping(value = "/updateCatPicture")
	public ResponseEntity<String> updateCatPicture(@RequestParam("id") Long id,
			@RequestParam("file") MultipartFile file) {

		try {
			Storage.getInstance().saveFile(CAT_IMAGES_HOME_PATH + File.separator, id, file);
			String message = "Successfully updated " + file.getOriginalFilename() + ", ID:" + id + ".";

			return ResponseEntity.ok(message);

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return ResponseEntity.badRequest().body(e.toString());
		}
	}

	// This will take an id parameter from the path, it will delete the image with
	// the id given
	// if the id is not present it will return successfully with a message that
//the image could not be deleted
	@DeleteMapping(value = "/deleteCatPicById/{id}")
	public ResponseEntity<String> deleteCatImage(@PathVariable Long id) {

		try {
			LOGGER.info("Deleting cat pic wit Id of" + id);

			Path path = Paths.get(CAT_IMAGES_HOME_PATH + File.separator + id + ".png");
			Resource resource = new UrlResource(path.toUri());

			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}

			boolean isDeleted = Storage.getInstance().deleteFile(CAT_IMAGES_HOME_PATH, id);
			String message = isDeleted ? "Image: " + id + " successFully deleted." : "Could not delete Image: " + id;

			return ResponseEntity.ok().body(message);

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return ResponseEntity.badRequest().build();
		}
	}

}