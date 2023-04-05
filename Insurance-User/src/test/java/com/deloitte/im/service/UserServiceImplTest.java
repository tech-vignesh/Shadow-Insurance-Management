package com.deloitte.im.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.deloitte.im.exception.UserAlreadyExistException;
import com.deloitte.im.exception.UserNotFoundException;
import com.deloitte.im.exception.UserServiceException;
import com.deloitte.im.model.Policy;
import com.deloitte.im.model.User;
import com.deloitte.im.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private Policy policy;

	@InjectMocks
	private UserServiceImpl userService;

	private User user;
	private User user2;

	private String userId;

	private String policyId;

	@Value("${policy.service.url}")
	private String policyUrl;

	@BeforeEach
	void setUp() throws Exception {

		user = new User();
		policy = new Policy();
		
		user.setId("1");
		user.setFirstName("Vicky");
		user.setLastName("Vignesh");
		user.setEmail("v@g.com");
		user.setPhone("1234567890");
		user.setAddress("123 USA St, USA");
		
		policy.setId("100");
		policy.setPolicyNumber("Hl1001");
		policy.setPolicyType("Health Insurance");
		policy.setPremiumAmount(new BigDecimal(23456333.99));
		Date startDate = new Date();
		Date endDate = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 365));
		policy.setStartDate(startDate);
		policy.setEndDate(endDate);
		List<Policy> policies = new ArrayList<>();
		policies.add(policy);
		user.setPolicies(policies);

		Policy policy1 = new Policy();
		policy1.setId("100");
		policy1.setPolicyNumber("Hl1001");
		policy1.setPolicyType("Health Insurance");
		policy1.setPremiumAmount(new BigDecimal(23456333.99));
		Date startDate1 = new Date();
		Date endDate1 = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 365));
		policy1.setStartDate(startDate1);
		policy1.setEndDate(endDate1);
		
		policies.add(policy1);
		user.setPolicies(policies);
		user2 = new User("1", "Sarath", "ss", "s@w.com", "2345678901", "123 Dubai St, Dubai", policies);
		userId = "1";
		policyId = "100";
	}

	@Test
	void testCreateUser() throws Exception {
        when(userRepository.save(user)).thenReturn(user);
        ResponseEntity<String> response = userService.createUser(user);
        assertEquals(HttpStatus.OK, response.getStatusCode()); 
        assertEquals("User added with ID : 1", response.getBody());
	}

	@Test()
	void testCreateUserUserAlreadyExists() {
		
		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		
		Exception exception = assertThrows(UserAlreadyExistException.class, ()->{
			userService.createUser(user);
			});
		String expectedMessage = "User with Email  '"+user.getEmail()+"' already exists";
		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	void testGetAllUsers() throws Exception {
		List<User> userList = new ArrayList<>();
		userList.add(user);
		userList.add(user2);
		when(userRepository.findAll()).thenReturn(userList);
		ResponseEntity<List<User>> expectedResponse = userService.getAllUsers();
		assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());
		assertEquals(2, expectedResponse.getBody().size());
	}

	@Test
	void testGetUserById() throws Exception {
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userService.getUserById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
	}

	@Test
	void testGetUserByIdUserNotFound() {
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		Exception exception = assertThrows(UserNotFoundException.class, ()->userService.getUserById(userId));
		assertEquals("User with id '"+userId+"' not found", exception.getMessage());
	}

	@Test
	void testUpdateUser() throws Exception {

		User updatedUser = new User();
		updatedUser.setFirstName("Vignesh");
		updatedUser.setLastName("vicky");
		updatedUser.setEmail("v@g.com");
		updatedUser.setPhone("1234567890");
		updatedUser.setAddress("123 USA St, USA");
		updatedUser.setPolicies(new ArrayList<>());

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userRepository.save(user)).thenReturn(user);
		ResponseEntity<String> response = userService.updateUser(userId, updatedUser);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("User with Id 1 updated!", response.getBody());
		assertEquals(updatedUser.getFirstName(), user.getFirstName());
		assertEquals(updatedUser.getLastName(), user.getLastName());
		assertEquals(updatedUser.getEmail(), user.getEmail());
		assertEquals(updatedUser.getPhone(), user.getPhone());
		assertEquals(updatedUser.getAddress(), user.getAddress());
		assertEquals(updatedUser.getPolicies(), user.getPolicies());
	}

	@Test
	void testUpdateUserUserNotFound() {
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		Exception exception = assertThrows(UserNotFoundException.class, ()->userService.updateUser(userId, user));
		assertEquals("User with id '"+userId+"' not found", exception.getMessage());
	}

	@Test
	void testDeleteUser() throws Exception {
		 when(userRepository.findById(userId)).thenReturn(Optional.of(user));
	     ResponseEntity<Boolean> response = userService.deleteUser(userId);
	     assertEquals(HttpStatus.OK, response.getStatusCode());
	     assertTrue(response.getBody());
	     verify(userRepository).deleteById(userId);
	}

	@Test
	void testDeleteUserUserNotFound() {
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		Exception exception = assertThrows(UserNotFoundException.class, ()->userService.deleteUser(userId));
		assertEquals("User with id '"+userId+"' not found", exception.getMessage());
	}

	@Test
	void testAddPolicyToUser() throws Exception {

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(restTemplate.getForObject(anyString(), eq(Policy.class))).thenReturn(policy);
		ResponseEntity<?> response = userService.addPolicyToUser(userId,"1235");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testAddPolicyToUserUserNotFound() {
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		Exception exception = assertThrows(UserNotFoundException.class, ()->userService.addPolicyToUser(userId, policyId));	
		assertEquals("User with id '"+userId+"' not found", exception.getMessage());
	}

	@Test
	void testAddPolicyToUserPolicyNotFound() { 
		when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
		when(restTemplate.getForObject(anyString(), eq(Policy.class))).thenReturn(null);
		Exception exception = assertThrows(UserServiceException.class, ()->userService.addPolicyToUser(userId, policyId));
		assertEquals("Policy with Id " + policyId + " not found!", exception.getMessage());
	}

	@Test
	void testAddPolicyToUserPolicyAlreadyExists() {
		String userId = "1"; 
		String policyId = "100";
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(restTemplate.getForObject(policyUrl + "/" + policyId, Policy.class)).thenReturn(policy);
		ResponseEntity<?> response = userService.addPolicyToUser(userId, policyId);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Policy with Id " + policyId + " is already added to user with Id " + userId + "!",
				response.getBody());
		assertTrue(user.getPolicies().contains(policy));
	}

}
