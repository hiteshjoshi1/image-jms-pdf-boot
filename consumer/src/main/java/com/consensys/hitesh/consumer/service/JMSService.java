package com.consensys.hitesh.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.consensys.hitesh.consumer.jms.JMSPdfMessagePublisher;

/**
 * 
 * @author hitjoshi
 *
 */
@Component
public class JMSService {
	
    @Autowired
    JMSPdfMessagePublisher jmsPdfMessagePublisher;
    
    public void sendToQueue(String test ) {
    	jmsPdfMessagePublisher.pushImageRequest(test);
    }
}
