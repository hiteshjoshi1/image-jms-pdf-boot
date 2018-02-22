package com.consensys.hitesh.producer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.consensys.hitesh.producer.model.ImageRequestDTO;

/**
 * @author hitjoshi
 *
 */
public interface ImageRepository extends MongoRepository<ImageRequestDTO, String> {

	  public List<ImageRequestDTO> findByImageOwner(String imageOwner);
	  
	

}