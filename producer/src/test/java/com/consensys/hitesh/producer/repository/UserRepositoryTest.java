package com.consensys.hitesh.producer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.consensys.hitesh.producer.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    
    User user1, user2, user3, user4;
    
    @Before
    public void setUp() {
    	user1 = userRepository.save(new User("testTest","test","hiteshjoshi1@yahoo.co.in"));
    	user2 = userRepository.save(new User("RestTest","best","sendmailtojoshi@gmail.com"));
    	user3 = userRepository.save(new User("BestLest","Rest","achiever.hitesh@gmail.com"));
    	user4 = userRepository.save(new User("Weed","indeed","mari@juana.com"));
    	
    }
	@Test
	public void testFindByUsername() {
		User user = userRepository.findByUsername("RestTest");
	    assertThat(user).extracting("email").contains("sendmailtojoshi@gmail.com");
	}

	@Test
	public void testFindByEmail() {
		User user = userRepository.findByEmail("hiteshjoshi1@yahoo.co.in");
	    assertThat(user).extracting("username").contains("testTest");
	}

	// save all
	@Test
	public void testSaveIterableOfS() {
		List<User> userList = new ArrayList<>();
		userList.add(user4);
		userRepository.save(userList);
		User user = userRepository.findByUsername("Weed");
		assertThat(user).extracting("email").contains("mari@juana.com");
	}

	@Test
	public void testFindAll() {
		List<User> userList = userRepository.findAll();
		assertThat(userList.contains(user1));
		assertThat(userList.contains(user2));
		assertThat(userList.contains(user3));
		
	}	
    @After
    public void destory() {
    	userRepository.delete(user1);
    	userRepository.delete(user2);
    	userRepository.delete(user3);
    	
    }

}
