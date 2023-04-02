package com.deloitte.im.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deloitte.im.model.User;

@Service
public interface UserService {
	
	ResponseEntity<String> createUser(User user);
	
	ResponseEntity<List<User>> getAllUsers();
	
	ResponseEntity<User> getUserById(String id);
	
	ResponseEntity<String> updateUser(String id,User user);
	
	ResponseEntity<Boolean> deleteUser(String id);
	
	ResponseEntity<?> addPolicyToUser(String userId, String policyId);
	
	

}