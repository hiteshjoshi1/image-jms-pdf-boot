package com.consensys.hitesh.consumer.converter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.consensys.hitesh.consumer.model.ImageDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author hitjoshi
 */
@Component
public class ImageToPdfConverter {

	private static final Logger logger = LoggerFactory.getLogger(ImageToPdfConverter.class.getName());

	public static final String MAIN_PDF_FILE_NAME = "final.pdf";

	public static final String WORKING_FILE_NAME = "temp.pdf";

	public static final String PDF_FOLDER_NAME = "pdfFolder";

	final String home = System.getProperty("user.home");

	private static final int POSITION_X_COORDINATE = 10;
	private static final int POSITION_Y_COORDINATE = 10;

	/**
	 * Adds to PDF if it exists Creates a new one if no pdf exists
	 * 
	 * @param imageName
	 * @throws DocumentException
	 * @throws IOException
	 * @returns true if the operation is successful.
	 */
	public boolean addtoPdf(ImageDTO imageDTO) throws IOException, DocumentException {
		// Get OS file seprator
		String fileSeprator = File.separator;
		String directoryPath = home + fileSeprator + PDF_FOLDER_NAME;
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdir();
		}
		// Generate Absolute paths for files
		String pdfFilePath = directoryPath + fileSeprator + MAIN_PDF_FILE_NAME;
		String tempPDFFilePath = directoryPath + fileSeprator + WORKING_FILE_NAME;
		File file = new File(pdfFilePath);

		// if PDF file exists already
		boolean fileUpdated = false;
		String imagePath = imageDTO.getImageLocation();

		if (file.exists()) {
			logger.info("Pdf exists, Updating PDF ");
			fileUpdated = updatePDF(pdfFilePath, tempPDFFilePath, imagePath);
			if (fileUpdated) {
				// rename the temp PDF to new PDF, delete existing
				updatePdfToLatest(pdfFilePath, tempPDFFilePath);
			}

		} else {
			// No PDF exists - first time creation in the application
			logger.info("Pdf does not exist , create one");
			fileUpdated = createPDF(imagePath, pdfFilePath);

		}
		return fileUpdated;
	}

	// New pdf creation
	private boolean createPDF(String imagePath, String pdfLocation)
			throws DocumentException, MalformedURLException, IOException {
		FileOutputStream fileOutputStream = null;
		Document document = null;
		PdfWriter writer = null;
		try {
			fileOutputStream = new FileOutputStream(pdfLocation);
			Image image = Image.getInstance(imagePath);
			// Using a standard A4 size container
			// This may lead to wastage of space for smaller images.
			// TODO - Find a better way to fit the images
			image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
			image.setAbsolutePosition(POSITION_X_COORDINATE, POSITION_Y_COORDINATE);
			document = new Document();
			writer = PdfWriter.getInstance(document, fileOutputStream);
			document.open();
			document.newPage();
			document.add(image);
		} finally {
			fileOutputStream.flush();
			document.close();
			writer.close();
		}
		return true;
	}

	// existing pdf uodate using a temp pdf
	private boolean updatePDF(String src, String dest, String img) throws IOException, DocumentException {
		PdfReader pdfReader = null;
		PdfStamper pdfStamper = null;
		FileOutputStream fos = new FileOutputStream(dest);
		try {
			pdfReader = new PdfReader(src);
			pdfStamper = new PdfStamper(pdfReader, fos);
			Image image = Image.getInstance(img);
			pdfStamper.insertPage(pdfReader.getNumberOfPages() + 1, pdfReader.getPageSize(1));
			image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
			image.setAbsolutePosition(POSITION_X_COORDINATE, POSITION_Y_COORDINATE);
			PdfContentByte pdfContentByteUnder = pdfStamper.getUnderContent(pdfReader.getNumberOfPages());
			pdfContentByteUnder.addImage(image);
			return true;
		} finally {
			fos.flush();
			pdfStamper.close();
			pdfReader.close();
		}
	}

	// rewriting temp pdf back to main pdf
	private void updatePdfToLatest(String mainPdf, String tmpPdf) throws IOException {
		logger.info("Replacing the old file with the latest");
		File oldfile = new File(mainPdf);
		File newFile = new File(tmpPdf);
		if (oldfile.delete()) {
			logger.info("Main Pdf file deleted ");
			if (newFile.renameTo(oldfile)) {
				logger.info("Generated file renamed to main file");

			}
		}

	}
}
