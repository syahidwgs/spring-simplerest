package com.syhdmf.simplerest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syhdmf.simplerest.dto.UserDto;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@Test
	public void save() throws Exception {
		UserDto request = newUser();
		
		System.out.println("1. save user");
		mockMvc.perform(post("/user")
				.content(mapper.writeValueAsString(request)).
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	public UserDto newUser() {
		UserDto request = new UserDto();
		Long ltimestamp = System.currentTimeMillis();
		String name = "Syahid";
		request.setUsername(name + ltimestamp);
		request.setEmail(name + ltimestamp + "@mail.com");
		request.setPassword("test");
		request.setPassword2("test");
		request.setFirstName(name);
		request.setLastName("mf");
		
		return request;
	}
}
