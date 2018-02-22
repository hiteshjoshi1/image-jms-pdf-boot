package com.consensys.hitesh.producer.api;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.consensys.hitesh.producer.constants.ProducerConstants;
import com.consensys.hitesh.producer.model.ImageRequestDTO;
import com.consensys.hitesh.producer.repository.ImageRepository;
import com.consensys.hitesh.producer.services.StorageService;

import io.swagger.annotations.Api;

@RestController
@Api(value = " ImageAPI", description = "Api for Image Upload/View")
public class DownloadController {

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	StorageService storageService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class.getName());

	private static final String CACHE_CONTROL = "Cache-Control";
	private static final String NO_CACHE_STORE_MUST_REVALIDATE = "no-cache, no-store, must-revalidate";
	private static final String PRAGMA_DIRECTIVE = "Pragma";
	private static final String EXPIRES = "Expires";
	private static final String NO_CACHE = "no-cache";

	/**
	 * Allows PDF to be opened in a new TAB We are not sending file as an attachment
	 * here
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/view/pdf", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamResource> openPdfFile() throws IOException {
		Path pdfPath = Paths.get(ProducerConstants.PDF_FULL_PATH);
		Resource finalPDF = storageService.loadFile(ProducerConstants.FINAL_PDF_NAME, pdfPath);

		HttpHeaders headers = new HttpHeaders();
		headers.add(CACHE_CONTROL, NO_CACHE_STORE_MUST_REVALIDATE);
		headers.add(PRAGMA_DIRECTIVE, NO_CACHE);
		headers.add(EXPIRES, "0");

		return ResponseEntity.ok().headers(headers).contentLength(finalPDF.contentLength())
				.contentType(MediaType.parseMediaType("application/pdf"))
				.body(new InputStreamResource(finalPDF.getInputStream()));

	}

	/**
	 * Downloads PDF as an attachment
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/download/pdf", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamResource> downloadPdfFile() throws IOException {
		Path pdfPath = Paths.get(ProducerConstants.PDF_FULL_PATH);
		Resource finalPDF = storageService.loadFile(ProducerConstants.FINAL_PDF_NAME, pdfPath);

		HttpHeaders headers = new HttpHeaders();
		headers.add(CACHE_CONTROL, NO_CACHE_STORE_MUST_REVALIDATE);
		headers.add(PRAGMA_DIRECTIVE, NO_CACHE);
		headers.add(EXPIRES, "0");
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + finalPDF.getFilename().toString());

		return ResponseEntity.ok().headers(headers).contentLength(finalPDF.contentLength())
				.contentType(MediaType.parseMediaType("application/pdf"))
				.body(new InputStreamResource(finalPDF.getInputStream()));

	}

	/**
	 * Download Image Not used - Can be used if we decide to make images as
	 * thumbnails in view all page
	 * 
	 * @param imageId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/view/img/{imageName}", method = RequestMethod.GET, produces = {
			MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<InputStreamResource> viewImage(@PathVariable("imageName") String imageName)
			throws IOException {
		Path imagePath = Paths.get(ProducerConstants.IMAGES_FULL_PATH);
		Resource image = storageService.loadFile(imageName, imagePath);
		HttpHeaders headers = new HttpHeaders();
		headers.add(CACHE_CONTROL, NO_CACHE_STORE_MUST_REVALIDATE);
		headers.add(PRAGMA_DIRECTIVE, NO_CACHE);
		headers.add(EXPIRES, "0");

		return ResponseEntity.ok().headers(headers).contentLength(image.contentLength())
				.body(new InputStreamResource(image.getInputStream()));
	}

	/**
	 * Get A list of all files as JSON that are uploaded by a User Redirects to
	 * image.html Where the JSON is iterated and used to show the Images Stream API
	 * usage and lamba expression
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/getallfiles")
	public ModelAndView messages(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();

		List<ImageRequestDTO> imageRequestDTOs = imageRepository.findByImageOwner(principal.getName());
		List<String> files = new ArrayList<String>();

		for (ImageRequestDTO imageRequestDTO : imageRequestDTOs) {
			files.add(imageRequestDTO.getImageName());
		}
		logger.info("Please note the usage of stream API here");
		List<String> fileNames = files
				.stream().map(fileName -> MvcUriComponentsBuilder
						.fromMethodName(DownloadController.class, "getFile", fileName).build().toString())
				.collect(Collectors.toList());
		ModelAndView modelAndView = new ModelAndView("images");
		modelAndView.addObject("messages", fileNames);
		return modelAndView;
	}

	/**
	 * Download individual files
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Path imagePath = Paths.get(ProducerConstants.IMAGES_FULL_PATH);
		Resource file = storageService.loadFile(filename, imagePath);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
