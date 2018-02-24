package com.consensys.hitesh.consumer.jms;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.consensys.hitesh.consumer.model.ImageDTO;
import com.consensys.hitesh.consumer.service.ImageProcessorService;
import com.consensys.hitesh.consumer.service.JMSService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author hitjoshi
 *
 */
@Component
public class JmsConsumer {

	@Autowired
	ImageProcessorService imageProcessorService;
	
	@Autowired
	JMSService jmsService;

	private static final Logger logger = LoggerFactory.getLogger(JmsConsumer.class.getName());

	/**
	 * Receive from Image Queue directly from consumer
	 * @param jsonMessage
	 * @throws JMSException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@JmsListener(destination = "${activemq.image.queue}", containerFactory = "listenerConnFactory")
	public void receiveMessage(final Message jsonMessage)
			throws JMSException, JsonParseException, JsonMappingException, IOException {
		String messageData = null;
		if (jsonMessage instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) jsonMessage;
			messageData = textMessage.getText();
			logger.info("Image Message recived  {}", messageData);
			ObjectMapper mapper = new ObjectMapper();
			ImageDTO imageDTO = mapper.readValue(messageData, ImageDTO.class);						
			// convert to PDF AND STORE
			String pdfPath = imageProcessorService.createPDFFromImage(imageDTO);
			// Send PDF merge Request to PDF Queue
			if(null != pdfPath) {
			jmsService.sendToQueue(pdfPath);
			}
		}
	}

	/**
	 * Receive from PDF Queue, consume the message sent above
	 * @param jsonMessage
	 * @throws JMSException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@JmsListener(destination = "${activemq.pdf.queue}", containerFactory = "listenerConnFactory")
	public void receiveMessageForPDF(final Message jsonMessage)
			throws JMSException, JsonParseException, JsonMappingException, IOException {
		String messageData = null;
		if (jsonMessage instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) jsonMessage;
			messageData = textMessage.getText();
			logger.info("Finally PDf conversion request  "+messageData);
			
			boolean isPDFUpdated = imageProcessorService.mergePDFs(messageData);
			logger.info("Processsing complete "+isPDFUpdated);

		}
	}

}
