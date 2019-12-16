package com.syhdmf.simplerest.seeder;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.syhdmf.simplerest.model.UserModel;
import com.syhdmf.simplerest.repository.UserRepository;

@Component
public class DatabaseSeeder {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@PostConstruct
	public void seed() {
		userSeed();
	}
	
	public void userSeed() {
		if (userRepo.count() == 0) {
			UserModel user = new UserModel();
			user.setUsername("syahidmf");
			user.setEmail("syahidmf@email.com");
			user.setPassword(encoder.encode("test1234"));
			user.setFirstName("Syahid");
			user.setLastName("mf");
			
			userRepo.save(user);
		}
	}
}
