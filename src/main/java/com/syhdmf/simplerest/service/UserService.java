package com.syhdmf.simplerest.service;

import org.springframework.data.domain.Page;

import com.syhdmf.simplerest.dto.UserDto;
import com.syhdmf.simplerest.model.UserModel;

public interface UserService {

	public void save(UserDto request) throws Exception;
	
	public Page<UserModel> findAll(int page, int limit);
	
	public UserModel findById(Long id);
	
	public void update(Long id, UserDto request) throws Exception;
	
	public void deleteById(Long id);
}
