package com.consensys.hitesh.producer.api;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.consensys.hitesh.producer.constants.ProducerConstants;
import com.consensys.hitesh.producer.model.ImageRequestDTO;
import com.consensys.hitesh.producer.repository.ImageRepository;
import com.consensys.hitesh.producer.services.JmsService;
import com.consensys.hitesh.producer.services.StorageService;

@Controller
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class.getName());

	private static final String DOT_SEPRATOR_REGEX = "\\.(?=[^\\.]+$)";
	private static final String UNDERSCORE = "_";
	private static final String DOT = ".";
	private static final String FILE_IS_EMPTY = "File is empty";
	private static final String FILE_UPLOAD_SUCCESSFUL = "You successfully uploaded file ";
	private static final String NOT_SUPPORTED_FORMAT = "Only jpg/png file types are supported";

	@Autowired
	JmsService jmsService;

	@Autowired
	StorageService storageService;

	@Autowired
	ImageRepository imageRepository;

	@PostMapping(value = "/post")
	public ModelAndView handleFileUpload(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		ModelAndView modelAndView = null;
		String message = validate(file);
		if (null != message) {
			modelAndView = new ModelAndView("upload", HttpStatus.BAD_REQUEST);
			modelAndView.addObject("message", message);
			return modelAndView;
		}
		String imageFileName = file.getOriginalFilename();
		Random random = new Random();
		// appending Random Numbers to avoid file Name collisions
		int randomGenerator = random.nextInt(100000);
		String[] tokens = imageFileName.split(DOT_SEPRATOR_REGEX);
		String imageWithRandomName = tokens[0]+ UNDERSCORE+randomGenerator;
		imageFileName = imageWithRandomName + DOT + tokens[1];
		String completeImagePath = ProducerConstants.IMAGES_FULL_PATH + File.separator + imageFileName;
		Path imagePath = Paths.get(ProducerConstants.IMAGES_FULL_PATH);
		try {
			if (storageService.store(file, imageFileName, imagePath)) {
				message = FILE_UPLOAD_SUCCESSFUL + file.getOriginalFilename();
				ImageRequestDTO imageRequestDTO = new ImageRequestDTO(completeImagePath,imageFileName,imageWithRandomName,
						principal.getName(), Calendar.getInstance().getTime());
				// save to Mongo DB
				imageRepository.save(imageRequestDTO);
				// send to JMS queue
				jmsService.sendToQueue(imageRequestDTO);
			}
			modelAndView = new ModelAndView("upload", HttpStatus.OK);
			modelAndView.addObject("success", message);
		} catch (Exception e) {
			logger.error("Upload Failed with exception ", e);
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			modelAndView = new ModelAndView("upload", HttpStatus.EXPECTATION_FAILED);
			modelAndView.addObject("message", message);
		}
		return modelAndView;
	}

	private String validate(MultipartFile file) {
		String validator = null;
		if (file.isEmpty() || file.getSize() == 0) {
			validator = FILE_IS_EMPTY;
		} else if (!(file.getContentType().toLowerCase().equals("image/jpg")
				|| file.getContentType().toLowerCase().equals("image/jpeg")
				|| file.getContentType().toLowerCase().equals("image/png"))) {
			validator = NOT_SUPPORTED_FORMAT;
		}
		return validator;

	}

	@GetMapping("/upload")
	public String index() {
		return "upload";
	}

}
