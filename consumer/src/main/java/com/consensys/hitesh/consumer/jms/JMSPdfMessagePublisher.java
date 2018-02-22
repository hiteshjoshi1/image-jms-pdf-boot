package com.consensys.hitesh.consumer.jms;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author hitjoshi
 *
 */
@Component
public class JMSPdfMessagePublisher {

	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	Queue queue;
	
	 private static final Logger logger =  LoggerFactory.getLogger(JMSPdfMessagePublisher.class
	            .getName());

	
	public void pushImageRequest(String path){
		logger.info("Sending PDF JMS Messages....."+path);
		jmsTemplate.convertAndSend(queue, path);

	}
}
