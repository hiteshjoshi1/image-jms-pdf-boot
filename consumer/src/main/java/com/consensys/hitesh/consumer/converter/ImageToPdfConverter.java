package com.consensys.hitesh.consumer.converter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.consensys.hitesh.consumer.model.ImageDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
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

	public static final String TEMP_PDF_FOLDER_NAME = "allPdfFolder";

	public static final String FILE_SEPRATOR = File.separator;
	
	public static final String PDF_EXTENSION = ".pdf";
	
	@Value("${user.pdfFolderName}")
	private String pdfFolderName;	
	
	@Value("${user.baseDir}")
	private String home;	

	private static final int POSITION_X_COORDINATE = 10;
	private static final int POSITION_Y_COORDINATE = 10;

	/**
	 * Converts Image Request coming from JMS to a PDF and store it in TEMP_PDF_FOLDER_NAME
	 * @param imageDTO
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String imagetoPDF(ImageDTO imageDTO) throws IOException, DocumentException {
		String fullQualifiedPDFPath = null;

		String directoryPath = home + FILE_SEPRATOR + TEMP_PDF_FOLDER_NAME;
		File directory = new File(directoryPath);
		// check if the pdf store location dir is available if not create
		if (!directory.exists()) {
			directory.mkdir();
		}
		String imagePath = imageDTO.getImageLocation();

		fullQualifiedPDFPath = directoryPath + FILE_SEPRATOR + imageDTO.getImagePdfName()+PDF_EXTENSION;

		createPDF(imagePath, fullQualifiedPDFPath);
		return fullQualifiedPDFPath;
	}

	/**
	 * Merge  new Incoming PDF with Application Wide PDF
	 * @param uploadedPDFPath
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public boolean mergePdfs(String uploadedPDFPath) throws IOException, DocumentException {
		String mainPDFFile = home + FILE_SEPRATOR + pdfFolderName + FILE_SEPRATOR + MAIN_PDF_FILE_NAME;
		String tempFile = home + FILE_SEPRATOR + pdfFolderName + FILE_SEPRATOR + WORKING_FILE_NAME;
		return updatePDFByPDF(uploadedPDFPath, mainPDFFile, tempFile);

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

	private boolean updatePDFByPDF(String srcPDF, String mainPDFFile, String tempFile)
			throws IOException, DocumentException {
		Document document = null;
		PdfReader newPDF = null;
		PdfReader mainPDF = null;
		PdfCopy pdfCopy = null;

		try {
			newPDF = new PdfReader(srcPDF);
			document = new Document();
			pdfCopy = new PdfCopy(document, new FileOutputStream(tempFile));
			document.open();

			File file = new File(mainPDFFile);
			if (!file.exists()) {
				// create the app wide file
				file.createNewFile();
				// copy only the new file
				pdfCopy.addDocument(newPDF);
			} else {
				mainPDF = new PdfReader(mainPDFFile);
				// Add global PDF to temp pdf
				pdfCopy.addDocument(mainPDF);
				// Add new PDF to temp pdf
				pdfCopy.addDocument(newPDF);
			}

			return true;
		} finally {
			document.close();
			newPDF.close();
			if (mainPDF != null)
				mainPDF.close();
			pdfCopy.close();
			updatePdfToLatest(mainPDFFile, tempFile);
		}

	}

	// Replace the old pdf with the new PDF generated post merge
	private void updatePdfToLatest(String mainPdf, String tmpPdf) throws IOException {
		logger.info("Replacing the old file with the updated file");
		File oldfile = new File(mainPdf);
		File newFile = new File(tmpPdf);
		if (oldfile.delete()) {
			logger.info("Main Pdf file deleted ");
			if (newFile.renameTo(oldfile)) {
				logger.info("Generated file renamed to main file");

			}
		}

	}
	
	/**
	 * Adds to PDF if it exists Creates a new one if no pdf exists
	 *  NOT used - This method was written for one Queue
	 * @param imageName
	 * @throws DocumentException
	 * @throws IOException
	 * @returns true if the operation is successful.
	 */
	public boolean addtoPdf(ImageDTO imageDTO) throws IOException, DocumentException {
		// Get OS file seprator
		String directoryPath = home + FILE_SEPRATOR + pdfFolderName;
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdir();
		}
		// Generate Absolute paths for files
		String pdfFilePath = directoryPath + FILE_SEPRATOR + MAIN_PDF_FILE_NAME;
		String tempPDFFilePath = directoryPath + FILE_SEPRATOR + WORKING_FILE_NAME;
		File file = new File(pdfFilePath);

		// if PDF file exists already
		boolean fileUpdated = false;
		String imagePath = imageDTO.getImageLocation();

		if (file.exists()) {
			logger.info("Pdf exists, Updating PDF.... ");
			fileUpdated = updatePDF(pdfFilePath, tempPDFFilePath, imagePath);
			if (fileUpdated) {
				// rename the temp PDF to new PDF, delete existing
				updatePdfToLatest(pdfFilePath, tempPDFFilePath);
			}

		} else {
			logger.info("Pdf does not exist , create one");
			fileUpdated = createPDF(imagePath, pdfFilePath);

		}
		return fileUpdated;
	}

	// Not used - existing pdf update using a temp pdf and by adding Images
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
}
