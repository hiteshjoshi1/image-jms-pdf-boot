package com.consensys.hitesh.producer.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.consensys.hitesh.producer.constants.ProducerConstants;
/**
 * Service for Image uploads and downloads
 * Did not program to an interface as I do not plan to extend this Service
 * @author hitjoshi
 *
 */

@Component
public class StorageService {

	private static final Logger logger =  LoggerFactory.getLogger(StorageService.class
			.getName());

	public boolean store(MultipartFile file, String generatedFileName, Path path) {
		boolean updated = false;
		
			long numBytes;
			try {
				numBytes = Files.copy(file.getInputStream(), path.resolve(generatedFileName));
			} catch (IOException e) {
				logger.error("Save failed with error ", e);
				throw new RuntimeException("FAIL!",e);
			}
			if(numBytes>0) updated = true;
		return updated;
	}

	public Resource loadFile(String filename, Path path) {
		try {
			Path file = path.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				logger.error("Could not find resource" );			
				throw new RuntimeException("FAILED :Could not find resource");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public void deleteAll(Path path) {
		FileSystemUtils.deleteRecursively(path.toFile());
	}

	public void init() {
		logger.info("Initializing Folder to store.");
		try {
			
			 File imgDirectory = new File(ProducerConstants.IMAGES_FULL_PATH);
			 File pdfDirectory = new File(ProducerConstants.PDF_FULL_PATH);
			 if (! imgDirectory.exists()){
				 Files.createDirectory(Paths.get(ProducerConstants.IMAGES_FULL_PATH));
			 }
			 if (! pdfDirectory.exists()){
				 Files.createDirectory(Paths.get(ProducerConstants.PDF_FULL_PATH));
			 }
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}
}
