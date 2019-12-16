package com.syhdmf.simplerest.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.syhdmf.simplerest.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

	@Query("select u from UserModel u where u.username = :username or u.email = :username")
	UserModel findLogin(String username);
	
	List<UserModel> findByUsernameOrEmail(String username, String email);

	Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);
}
