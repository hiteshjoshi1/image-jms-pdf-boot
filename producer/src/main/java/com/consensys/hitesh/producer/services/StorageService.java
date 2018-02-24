package com.consensys.hitesh.producer.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.consensys.hitesh.producer.constants.LocationProperties;
import com.consensys.hitesh.producer.model.ImageRequestDTO;
import com.consensys.hitesh.producer.repository.ImageRepository;
/**
 * Service for Image uploads and downloads
 * Did not program to an interface as I do not plan to extend this Service
 * @author hitjoshi
 *
 */

@Component
public class StorageService {
	
	private static final String DOT_SEPRATOR_REGEX = "\\.(?=[^\\.]+$)";
	private static final String UNDERSCORE = "_";
	private static final String DOT = ".";

	
	@Autowired
	LocationProperties locationProperties;
	
	@Autowired
	ImageRepository imageRepository;

	private static final Logger logger =  LoggerFactory.getLogger(StorageService.class
			.getName());
	
	public ImageRequestDTO transformAndStore(MultipartFile file, String userName) {
		String imageFileName = file.getOriginalFilename();
		Random random = new Random();
		// appending Random Numbers to avoid file Name collisions
		int randomGenerator = random.nextInt(100000);
		String[] tokens = imageFileName.split(DOT_SEPRATOR_REGEX);
		String imageWithRandomName = tokens[0]+ UNDERSCORE+randomGenerator;
		imageFileName = imageWithRandomName + DOT + tokens[1];
		String imagesFullPath = locationProperties.getHome()+File.separator+locationProperties.getImageFolderName();
		String completeImagePath = imagesFullPath + File.separator + imageFileName;
		Path imagePath = Paths.get(imagesFullPath);
		ImageRequestDTO imageRequestDTO = null;
		if (this.store(file, imageFileName, imagePath)) {
			imageRequestDTO = new ImageRequestDTO(completeImagePath,imageFileName,imageWithRandomName,
					userName, Calendar.getInstance().getTime());
			// save to Mongo DB
			imageRepository.save(imageRequestDTO);
		}
		return imageRequestDTO;						
	}
	

	private boolean store(MultipartFile file, String generatedFileName, Path path) {
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
			throw new RuntimeException("URL Malformed!");
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
