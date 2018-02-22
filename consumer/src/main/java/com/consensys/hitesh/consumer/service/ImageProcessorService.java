package com.consensys.hitesh.consumer.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consensys.hitesh.consumer.converter.ImageToPdfConverter;
import com.consensys.hitesh.consumer.model.ImageDTO;
import com.itextpdf.text.DocumentException;

@Service
public class ImageProcessorService {
	private static final Logger logger = LoggerFactory.getLogger(ImageProcessorService.class.getName());
	
	@Autowired
	ImageToPdfConverter imageToPdfConverter;
	public boolean updatePDF(ImageDTO imageDTO) {
		try {
			return imageToPdfConverter.addtoPdf(imageDTO);
		} catch (IOException | DocumentException e) {
			logger.error("Failed to add an image ",e);
			//TODO - throw a user generated error message
			return false;
		}
	}

}
