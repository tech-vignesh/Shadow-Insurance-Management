package com.deloitte.im.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.deloitte.im.exception.UserAlreadyExistException;
import com.deloitte.im.exception.UserNotFoundException;
import com.deloitte.im.exception.UserServiceException;
import com.deloitte.im.model.Policy;
import com.deloitte.im.model.User;
import com.deloitte.im.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@Mock
	private HttpServletRequest request;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateUser() throws Exception {

		logger.info("Initializing the dummy Objects");
		User user = new User();
		Policy policy = new Policy();
		policy.setId("456fgh567vgbh");
		user.setFirstName("Vicky");
		user.setLastName("Vignesh");
		user.setEmail("v@g.com");
		user.setPhone("1234567890");
		user.setAddress("123 USA St, USA");
		List<Policy> policies = new ArrayList<>();
		policies.add(policy);
		user.setPolicies(policies);

		when(userService.createUser(any(User.class)))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("User created successfully"));

		ResponseEntity<String> response = userController.createUser(user);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("User created successfully", response.getBody());
		verify(userService, times(1)).createUser(user);
	}

	@Test
	public void testCreateUserUserAlreadyExistException() throws UserAlreadyExistException {
		User user = new User("6nvkn1343", "vicky", "G", "v@g.com", "1234567890", "123 USA St", new ArrayList<>());
		when(userService.createUser(any(User.class))).thenThrow(UserAlreadyExistException.class);

		ResponseEntity<String> response = userController.createUser(user);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	@Test
	public void testCreateUser_UserServiceException() throws UserServiceException {
		User user = new User("6nvkn1343", "vicky", "G", "v@g.com", "1234567890", "123 USA St", new ArrayList<>());
		when(userService.createUser(any(User.class))).thenThrow(UserServiceException.class);

		ResponseEntity<String> response = userController.createUser(user);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void testGetAllUsers() throws Exception {
		List<User> users = new ArrayList<>();
		User user1 = new User("6nvkn1343", "vicky", "G", "v@g.com", "1234567890", "123 USA St", new ArrayList<>());
		User user2 = new User("6jiojesij", "vignesh", "G", "g@v.com", "0987654321", "456 Dubai St", new ArrayList<>());
		users.add(user1);
		users.add(user2);

		when(userService.getAllUsers()).thenReturn(ResponseEntity.ok(users));

		ResponseEntity<List<User>> response = userController.getAllUsers();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		assertEquals(user1, response.getBody().get(0));
		assertEquals(user2, response.getBody().get(1));
	}

	@Test
    public void testGetAllUsersUserServiceException() throws UserServiceException {
        
		when(userService.getAllUsers()).thenThrow(UserServiceException.class);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

	@Test
	public void testGetUserById() throws Exception {

		User mockUser = new User();
		mockUser.setId("6vecdsc123");
		mockUser.setFirstName("vicky");
		mockUser.setLastName("vignesh");
		mockUser.setEmail("v@g.com");
		mockUser.setPhone("1234567890");
		mockUser.setAddress("123 USA St, USA");

		when(userService.getUserById("6vecdsc123")).thenReturn(ResponseEntity.ok(mockUser));

		ResponseEntity<User> response = userController.getUserById("6vecdsc123");

		verify(userService, times(1)).getUserById("6vecdsc123");

		assertEquals(mockUser, response.getBody());
	}

	@Test
	public void testUpdateUser() throws Exception {
		User user = new User();
		Policy policy = new Policy();
		policy.setId("456fgh567vgbh");
		user.setId("6sdfghj123");
		user.setFirstName("Vicky");
		user.setLastName("Vignesh");
		user.setEmail("v@g.com");
		user.setPhone("1234567890");
		user.setAddress("123 USA St, USA");
		List<Policy> policies = new ArrayList<>();
		policies.add(policy);
		user.setPolicies(policies);

		when(userService.updateUser(anyString(), any(User.class)))
				.thenReturn(ResponseEntity.ok("User updated successfully"));

		ResponseEntity<String> response = userController.updateUser("6sdfghj123", user);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("User updated successfully", response.getBody());
		verify(userService, times(1)).updateUser("6sdfghj123", user);

	}

	@Test
	public void testUpdateUserUserNotFoundException() throws UserNotFoundException {
		User user = new User("6nvkn1343", "vicky", "G", "v@g.com", "1234567890", "123 USA St", new ArrayList<>());
		when(userService.updateUser(anyString(), any(User.class))).thenThrow(UserNotFoundException.class);

		ResponseEntity<String> response = userController.updateUser("1", user);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testUpdateUserUserServiceException() throws UserServiceException {
		User user = new User("6nvkn1343", "vicky", "G", "v@g.com", "1234567890", "123 USA St", new ArrayList<>());
		when(userService.updateUser(anyString(), any(User.class))).thenThrow(UserServiceException.class);

		ResponseEntity<String> response = userController.updateUser("1", user);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void testDeleteUser() throws Exception{
		
	   when(userService.deleteUser("6kbvrhoij3455")).thenReturn(ResponseEntity.ok(true));

	   ResponseEntity<String> response = userController.deleteUser("6kbvrhoij3455");

	   verify(userService, times(1)).deleteUser("6kbvrhoij3455");

	   assertEquals(HttpStatus.OK, response.getStatusCode());
	   assertEquals("User deleted", response.getBody());
	}

	@Test
    public void testDeleteUserUserNotFoundException() throws UserNotFoundException {
        when(userService.deleteUser(anyString())).thenThrow(UserNotFoundException.class);

        ResponseEntity<String> response = userController.deleteUser("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

	@Test
    public void testDeleteUserUserServiceException() throws UserServiceException {
        when(userService.deleteUser(anyString())).thenThrow(UserServiceException.class);

        ResponseEntity<String> response = userController.deleteUser("1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
	
	
	
	@Test
	public void testAddPolicyToUser() throws Exception {

		doReturn(ResponseEntity.ok().body("Policy added successfully")).when(userService).addPolicyToUser("1", "100");

		when(request.getParameter("userId")).thenReturn("1");
		when(request.getParameter("policyId")).thenReturn("100");

		ResponseEntity<?> response = userController.addPolicyToUser(request.getParameter("userId"),
				request.getParameter("policyId"));

		verify(userService).addPolicyToUser("1", "100");

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Policy added successfully", response.getBody());

	}

	@Test
	public void testAddPolicyToUser_UserNotFoundException() throws UserNotFoundException {
        when(userService.addPolicyToUser(anyString(), anyString())).thenThrow(UserNotFoundException.class);

        ResponseEntity<?> response = userController.addPolicyToUser("1", "1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
