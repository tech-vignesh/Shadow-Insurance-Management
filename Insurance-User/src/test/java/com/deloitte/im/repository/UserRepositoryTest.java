package com.deloitte.im.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.deloitte.im.model.Policy;
import com.deloitte.im.model.User;

@ExtendWith(MockitoExtension.class)
@DataMongoTest
class UserRepositoryTest {
	
	 @Autowired
	 private UserRepository userRepository;


	@Test
	void testFindByEmail() {
	
		User user = new User();
		Policy policy = new Policy();
		user.setFirstName("Vicky");
		user.setLastName("Vignesh");
		user.setEmail("v@g.com");
		user.setPhone("1234567890");
		user.setAddress("123 USA St, USA");
		List<Policy> policies = new ArrayList<>();
		policies.add(policy);
		user.setPolicies(policies);
        User foundUser = userRepository.findByEmail("v@g.com");
        assertNotNull(foundUser);
        assertEquals("v@g.com", foundUser.getEmail());
	}

}