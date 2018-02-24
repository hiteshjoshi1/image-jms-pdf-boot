package com.consensys.hitesh.producer.api;

import java.security.Principal;

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

import com.consensys.hitesh.producer.constants.LocationProperties;
import com.consensys.hitesh.producer.model.ImageRequestDTO;
import com.consensys.hitesh.producer.services.JmsService;
import com.consensys.hitesh.producer.services.StorageService;

import io.swagger.annotations.Api;

@Controller
@Api(value = " UploadApi", description = "Controller for Uploading images")
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class.getName());

	private static final String FILE_IS_EMPTY = "File is empty";
	private static final String FILE_UPLOAD_SUCCESSFUL = "You successfully uploaded file ";
	private static final String NOT_SUPPORTED_FORMAT = "Only jpg/png file types are supported";

	@Autowired
	StorageService storageService;

	@Autowired
	JmsService jmsService;

	@Autowired
	LocationProperties locationProperties;

	@PostMapping(value = "/upload")
	public ModelAndView handleFileUpload(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		ModelAndView modelAndView = null;
		String message = validate(file);
		if (null != message) {
			modelAndView = new ModelAndView("upload", HttpStatus.BAD_REQUEST);
			modelAndView.addObject("message", message);
			return modelAndView;
		}
		try {
			// save image
			ImageRequestDTO imageRequestDTO = storageService.transformAndStore(file, 
					principal.getName());
			// send mesg to JMS queue
			jmsService.sendToQueue(imageRequestDTO);
			// show response in UI
			message = FILE_UPLOAD_SUCCESSFUL + file.getOriginalFilename();
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

	// @PathMapping(consumes="") is not working hence this check
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
