package com.consensys.hitesh.producer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.consensys.hitesh.producer.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	   User findByUsername(String username);
	   User findByEmail(String email);
}
