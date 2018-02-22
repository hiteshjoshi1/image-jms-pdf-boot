package com.consensys.hitesh.producer.jms;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.consensys.hitesh.producer.model.ImageRequestDTO;

@Component
public class JmsProducer {
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	Queue queue;
	
	 private static final Logger logger =  LoggerFactory.getLogger(JmsProducer.class
	            .getName());

	
	public void pushImageRequest(ImageRequestDTO imageRequestDTO){
		logger.info("Sending JMS Messages....."+imageRequestDTO);
		jmsTemplate.convertAndSend(queue, imageRequestDTO);

	}
	

	

	
}