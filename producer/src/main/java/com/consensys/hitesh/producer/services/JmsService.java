package com.consensys.hitesh.producer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consensys.hitesh.producer.jms.JmsProducer;
import com.consensys.hitesh.producer.model.ImageRequestDTO;

/**
 * @author hitjoshi
 *
 */
@Service
public class JmsService {
	
    @Autowired
    JmsProducer jmsProducer;
    
    public void sendToQueue(ImageRequestDTO imageRequestDTO ) {
    	 	jmsProducer.pushImageRequest(imageRequestDTO);
    }
    
}
