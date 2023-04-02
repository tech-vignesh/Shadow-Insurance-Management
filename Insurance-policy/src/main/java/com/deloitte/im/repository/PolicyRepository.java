package com.deloitte.im.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.im.model.Policy;

@Repository
public interface PolicyRepository extends MongoRepository<Policy, String>{

}