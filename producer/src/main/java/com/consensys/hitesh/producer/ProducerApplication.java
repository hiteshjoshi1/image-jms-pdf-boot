package com.consensys.hitesh.producer;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import com.consensys.hitesh.producer.services.StorageService;
/**
 * Spring Boot main class
 * @author Hitesh Joshi
 *
 */
@SpringBootApplication
@EnableJms
public class ProducerApplication  implements CommandLineRunner {


	@Resource
	StorageService storageService;
	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		storageService.init();
	}
}
