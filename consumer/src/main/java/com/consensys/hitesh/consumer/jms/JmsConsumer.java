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

	private static final Logger logger = LoggerFactory.getLogger(JmsConsumer.class.getName());

	@JmsListener(destination = "${activemq.pdf.queue}", containerFactory = "listenerConnFactory")
	public void receiveMessage(final Message jsonMessage)
			throws JMSException, JsonParseException, JsonMappingException, IOException {
		String messageData = null;
			if (jsonMessage instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) jsonMessage;
			messageData = textMessage.getText();
			ObjectMapper mapper = new ObjectMapper();
			ImageDTO imageDTO = mapper.readValue(messageData, ImageDTO.class);
			boolean update = updatePDF(imageDTO);
			logger.info("Updation Status {}", update);
			if(!update) {
				//TODO - retry logic
			}
		}
	}

	private boolean updatePDF(ImageDTO imageDTO) {
		logger.info(" Image Processing started for {}",imageDTO.getImageName());
		return (imageProcessorService.updatePDF(imageDTO));

	}

}
