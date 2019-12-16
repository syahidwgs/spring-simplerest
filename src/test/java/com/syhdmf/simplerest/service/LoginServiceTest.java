package com.syhdmf.simplerest.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syhdmf.simplerest.dto.TokenDto;

@Service
public class LoginServiceTest {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	RestTemplateBuilder restTemplateBuilder;
	
	public void login(String username, String password) {
		restTemplate = restTemplateBuilder.build();
		
		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("simplerest","secret"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<String,String>();

        map.add("grant_type","password");
        map.add("username",username);
        map.add("password",password);

        final HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        
        ResponseEntity<TokenDto> response = restTemplate.exchange("/oauth/token", HttpMethod.POST, entity, TokenDto.class);

        TokenDto tokenDto = response.getBody();
        
        new UsernamePasswordAuthenticationToken(tokenDto, null);
        
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
}
