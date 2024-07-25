package com.mongodb.apirestmongodb.repository;

import com.mongodb.apirestmongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {
}
