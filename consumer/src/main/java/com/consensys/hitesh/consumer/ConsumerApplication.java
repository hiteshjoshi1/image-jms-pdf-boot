package com.consensys.hitesh.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * 
 * @author Hitesh Joshi Starting point for Spring boot consumer app
 */
@SpringBootApplication
@EnableJms
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

}
