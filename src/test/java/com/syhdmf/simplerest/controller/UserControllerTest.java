package com.syhdmf.simplerest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syhdmf.simplerest.dto.UserDto;
import com.syhdmf.simplerest.model.UserModel;
import com.syhdmf.simplerest.service.LoginServiceTest;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	LoginServiceTest loginService;
	
	private String accessToken = "test";
	
	@Test
	@Order(0)
	public void ainit() throws Exception {
		System.out.println("1. login");
		System.out.println("wrong password");
		dologin("syahidmf@email.com", "test12345").andExpect(status().is4xxClientError());
		System.out.println("right password");
		dologin("syahidmf@email.com", "test1234").andExpect(status().isOk());
	}
	
	private ResultActions dologin(String username, String password) throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "password");
	    params.add("username", username);
	    params.add("password", password);

		ResultActions result = mockMvc.perform(post("/oauth/token")
			.params(params)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
			.with(httpBasic("simplerest", "secret")));
	 
	 	return result;
	}
	
	private void setToken(ResultActions result) throws Exception {
		Map<String, Object> response = setMapResponse(result);
		this.accessToken = response.get("access_token").toString();
		System.out.println(this.accessToken);
	}
	
	private Map<String, Object> setMapResponse(ResultActions result) throws Exception {
		String resultString = result.andReturn().getResponse().getContentAsString();
		
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		Map<String, Object> response = jsonParser.parseMap(resultString);
		
		return response;
	}
	
	@Test
	@Order(1)
	public void bsave() throws Exception {
		System.out.println("login...");
		ResultActions result = dologin("syahidmf@email.com", "test1234");
		setToken(result);
		
		System.out.println("2. save user blank");
		UserDto request = new UserDto();
		dosave(request).andExpect(status().isBadRequest());

		request = newUser();
		String temp = "";
		
		System.out.println("3. save user username min length");
		temp = request.getUsername();
		request.setUsername("s");
		dosave(request).andExpect(status().isBadRequest());
		System.out.println(temp);
		request.setUsername(temp);
		
		System.out.println("4. save user duplicate email");
		temp = request.getEmail();
		request.setEmail("syahidmf@email.com");
		dosave(request).andExpect(status().isBadRequest());
		System.out.println(temp);
		request.setEmail(temp);
		
		System.out.println("5. save user password confirmation not matched");
		request.setPassword2("test12345");
		dosave(request).andExpect(status().isBadRequest());
		request.setPassword2("test1234");
		
		System.out.println("6. save user");
		dosave(request).andExpect(status().isOk());
		
		System.out.println("7. search saved user");
		search(request.getEmail()).andExpect(jsonPath("$.data.content", hasSize(1)));
	}

	public UserDto newUser() {
		UserDto request = new UserDto();
		Long ltimestamp = System.currentTimeMillis();
		String name = "syahid";
		request.setUsername(name + ltimestamp);
		request.setEmail(name + ltimestamp + "@email.com");
		request.setPassword("test1234");
		request.setPassword2("test1234");
		request.setFirstName(name);
		request.setLastName("mf");
		
		return request;
	}
	
	private ResultActions dosave(UserDto request) throws Exception {
		ResultActions result = mockMvc.perform(post("/user")
				.content(mapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
			.andDo(print());
		
		return result;
	}
	
	private ResultActions search(String q) throws Exception {
		ResultActions result = mockMvc.perform(get("/user/list?q=" + q)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
			.andDo(print());
		
		return result;
	}

	@Test
	@Order(2)
	public void cedit() throws Exception {
		System.out.println("login...");
		ResultActions result = dologin("syahidmf@email.com", "test1234");
		setToken(result);
		
		System.out.println("8. find no user");
		find((long)0).andExpect(status().isBadRequest());
		
		System.out.println("9. find user");
		result = find((long)1);
		result.andExpect(status().isOk());
		
		UserModel entity = setDetailUser(result);
		UserDto request = new UserDto(entity);
		String newFirstName = request.getFirstName() + "edit";
		request.setFirstName(newFirstName);
		
		System.out.println("10. update no user");
		update((long)0, request).andExpect(status().isBadRequest());
		
		System.out.println("11. update user");
		update((long)1, request).andExpect(status().isOk());
		
		System.out.println("12. find user");
		find((long)1).andExpect(jsonPath("$.data.firstName", is(newFirstName)));
	}

	private ResultActions find(Long id) throws Exception {
		ResultActions result = mockMvc.perform(get("/user/" + id)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
			.andDo(print());
		
		return result;
	}
	
	private UserModel setDetailUser(ResultActions result) throws Exception {
		Map<String, Object> response = setMapResponse(result);
		
		UserModel data = mapper.convertValue(response.get("data"), UserModel.class);
		
		return data;
	}
	
	private ResultActions update(Long id, UserDto request) throws Exception {
		ResultActions result = mockMvc.perform(put("/user/" + id)
				.content(mapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
			.andDo(print());
		
		return result;
	}
	
	@Test
	@Order(3)
	public void ddestroy() throws Exception {
		System.out.println("login...");
		ResultActions result = dologin("syahidmf@email.com", "test1234");
		setToken(result);
		
		System.out.println("prepare destroy");
		result = search("");
		List<Map<String, Object>> listUser = setListUser(result);
		int id = 0;
		System.out.println(listUser);
		for (Map<String, Object> row: listUser) {
			if (!row.get("userId").equals(1)) {
				id = (int)row.get("userId");
				break;
			}
		}
		System.out.println("13. destroy id " + id);
		if (id != 0) {
			destroy(id).andExpect(status().isOk());
		}
	}
	
	private List<Map<String, Object>> setListUser(ResultActions result) throws Exception {
		Map<String, Object> response = setMapResponse(result);
		Map<String, Object> responseData = mapper.convertValue(response.get("data"), Map.class);
		String jsonData = mapper.writeValueAsString(responseData.get("content"));
		List<Map<String, Object>> data = mapper.readValue(jsonData, List.class);

		return data;
	}
	
	private ResultActions destroy(int id) throws Exception {
		ResultActions result = mockMvc.perform(delete("/user/" + Long.valueOf(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
			.andDo(print());
		
		return result;
	}
}
