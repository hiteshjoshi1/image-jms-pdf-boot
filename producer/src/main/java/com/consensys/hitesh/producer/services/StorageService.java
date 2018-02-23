package com.consensys.hitesh.producer.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.consensys.hitesh.producer.constants.LocationProperties;
/**
 * Service for Image uploads and downloads
 * Did not program to an interface as I do not plan to extend this Service
 * @author hitjoshi
 *
 */

@Component
public class StorageService {
	
	@Autowired
	LocationProperties locationProperties;

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

	public Resource loadFile(String filename, Path path) throws FileNotFoundException {
		try {
			Path file = path.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				logger.error("Could not find resource" );			
				throw new FileNotFoundException("FAILED :Could not find the file");
			}
		} catch (MalformedURLException e) {
			logger.error("Load file Failed with error", e);
			throw new RuntimeException("FAIL!");
		}
	}

	public void deleteAll(Path path) {
		FileSystemUtils.deleteRecursively(path.toFile());
	}

	public void init() {
		String imagesFullPath = locationProperties.getHome()+File.separator+locationProperties.getImageFolderName();
		String pdfFullPath    = locationProperties.getHome()+File.separator+locationProperties.getPdfFolderName();
		try {
			
			 File imgDirectory = new File(imagesFullPath);
			 File pdfDirectory = new File(pdfFullPath);
			 if (! imgDirectory.exists()){
				 Files.createDirectory(Paths.get(imagesFullPath));
			 }
			 if (! pdfDirectory.exists()){
				 Files.createDirectory(Paths.get(pdfFullPath));
			 }
		} catch (IOException e) {
			logger.error("Init Failed with error", e);
			throw new RuntimeException("Could not initialize storage!");
		}
	}
}
