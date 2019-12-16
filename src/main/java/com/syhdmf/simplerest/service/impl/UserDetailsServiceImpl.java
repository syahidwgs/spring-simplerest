package com.syhdmf.simplerest.service.impl;

import org.springframework.stereotype.Service;

import com.syhdmf.simplerest.model.UserModel;
import com.syhdmf.simplerest.repository.UserRepository;
import com.syhdmf.simplerest.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel user = repo.findLogin(username);
		if (user == null) {
			throw new UsernameNotFoundException("User Not Found");
		}
		
		List<SimpleGrantedAuthority> listSga = new ArrayList<>();
		return new UserDetailsImpl(user, listSga);
	}

}
