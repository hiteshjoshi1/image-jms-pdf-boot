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

	/**
	 * Creating a PDF from Incoming Image
	 * @param imageDTO
	 * @return
	 */
	public String createPDFFromImage(ImageDTO imageDTO) {
		String pdfLocation = null;
		try {
			pdfLocation = imageToPdfConverter.imagetoPDF(imageDTO);
		} catch (IOException | DocumentException e) {
			logger.error("Failed to add an image ", e);
			// TODO - throw a user generated error message
		}
		return pdfLocation;
	}

	/**
	 * After receiving the merge request, merge new PDF with App wide PDF
	 * @param newPDFpath
	 * @return
	 */
	public boolean mergePDFs(String newPDFpath) {
		try {
		return imageToPdfConverter.mergePdfs(newPDFpath);
		} catch (IOException | DocumentException e) {
			logger.error("Merging PDF Failed ",e);
			e.printStackTrace();
			return false;
		}
	}

}
