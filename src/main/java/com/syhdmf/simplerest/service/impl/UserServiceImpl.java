package com.syhdmf.simplerest.service.impl;

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.syhdmf.simplerest.dto.UserDto;
import com.syhdmf.simplerest.model.UserModel;
import com.syhdmf.simplerest.repository.UserRepository;
import com.syhdmf.simplerest.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repo;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Transactional
	public void save(UserDto request) throws Exception {
		checkPass(request);
		UserModel entity = new UserModel(request);
		check(entity);
		entity.setPassword(encoder.encode(entity.getPassword()));
		repo.save(entity);
	}
	
	private void checkPass(UserDto request) throws Exception {
		if (request.getPassword() == null) {
			throw new Exception("password must not be null.");
		}
		if (request.getPassword2() == null) {
			throw new Exception("password2 must not be null.");
		}
		if (!request.getPassword().equals(request.getPassword2())) {
			throw new Exception("password confirmation must not be match.");
		}
	}
	
	private void check(UserModel entity) throws Exception {
		List<UserModel> data = repo.findByUsernameOrEmail(entity.getUsername(), entity.getEmail());
		for (UserModel row: data) {
			if (entity.getUserId() != null && entity.getUserId().equals(row.getUserId())) {
				continue;
			}
			if (row.getUsername().equals(entity.getUsername())) {
				throw new Exception("username " + entity.getUsername() + " already exist.");
			}
			if (row.getEmail().equals(entity.getEmail())) {
				throw new Exception("email " + entity.getEmail() + " already exist.");
			}
		}
	}
	
	public Page<UserModel> findAll(int page, int limit, String q) {
		Specification<UserModel> spec = null;
		if (!q.equals("")) {
			spec = filter(q);
		}
		Pageable pageable = PageRequest.of(page - 1, limit);
		return repo.findAll(spec, pageable);
	}
	
	private Specification<UserModel> filter(String q) {
		return (root, query, cb) ->{
			Predicate whereFinal = null;
			String qword = "%" + q + "%";
			Predicate where1 = cb.like(root.<String> get("username"), qword);
			Predicate where2 = cb.like(root.<String> get("email"), qword);
			Predicate where3 = cb.like(root.<String> get("firstName"), qword);
			Predicate where4 = cb.like(root.<String> get("lastName"), qword);
			
			whereFinal = cb.or(where1, where2, where3, where4);
			return whereFinal;
		};
	}

	public UserModel findById(Long id) {
		return repo.findById(id).orElse(null);
	}
	
	@Transactional
	public void update(Long id, UserDto request) throws Exception {
		checkPass2(request);
		UserModel entity = findById(id);
		if (entity == null) {
			throw new Exception("Data not found.");
		}
		entity.setFromDto(request);
		check(entity);
		if (request.getPassword() != null) {
			entity.setPassword(encoder.encode(entity.getPassword()));
		}
		repo.save(entity);
	}
	
	private void checkPass2(UserDto request) throws Exception {
		if (request.getPassword() != null && request.getPassword2() != null) {
			if (!request.getPassword().equals(request.getPassword2())) {
				throw new Exception("password confirmation must be match.");
			}
		}
	}
	
	@Transactional
	public void deleteById(Long id) {
		repo.deleteById(id);
	}
}
