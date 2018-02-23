package com.consensys.hitesh.producer.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocationProperties {

	@Value("${user.baseDir}")
	private String home;

	@Value("${user.pdfFolderName}")
	private String pdfFolderName;

	@Value("${user.imageFolderName}")
	private String imageFolderName;

	@Value("${user.finalPDFName}")
	private String finalPDFName;

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getPdfFolderName() {
		return pdfFolderName;
	}

	public void setPdfFolderName(String pdfFolderName) {
		this.pdfFolderName = pdfFolderName;
	}

	public String getImageFolderName() {
		return imageFolderName;
	}

	public void setImageFolderName(String imageFolderName) {
		this.imageFolderName = imageFolderName;
	}

	public String getFinalPDFName() {
		return finalPDFName;
	}

	public void setFinalPDFName(String finalPDFName) {
		this.finalPDFName = finalPDFName;
	}

}
