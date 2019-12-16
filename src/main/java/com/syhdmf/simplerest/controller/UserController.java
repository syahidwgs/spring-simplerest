package com.syhdmf.simplerest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syhdmf.simplerest.dto.UserDto;
import com.syhdmf.simplerest.dto.response.BaseResponse;
import com.syhdmf.simplerest.helper.JsonResponseUtil;
import com.syhdmf.simplerest.model.UserModel;
import com.syhdmf.simplerest.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService service;
	
	@PostMapping
	public ResponseEntity<BaseResponse<Object>> save(@Valid @RequestBody UserDto request, BindingResult result) {
		if (result.hasErrors()) {
			return JsonResponseUtil.error(result.getFieldErrors());
		}
		try {
			service.save(request);
		} catch (Exception e) {
			return JsonResponseUtil.error(null, e.getMessage());
		}
		return JsonResponseUtil.success(null);
	}
	
	@GetMapping("/list")
	public ResponseEntity<BaseResponse<Page<UserModel>>> list(@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "") String q) {	
		Page<UserModel> data = service.findAll(page, limit, q);
		return JsonResponseUtil.success(data);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<BaseResponse<UserModel>> getDetail(@PathVariable Long id) {
		UserModel data = service.findById(id);
		if (data == null) {
			return JsonResponseUtil.error(null, "Data not found.");
		}
		data.setPassword("");
		return JsonResponseUtil.success(data);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<BaseResponse<Object>> update(@PathVariable Long id, @Valid @RequestBody UserDto request, BindingResult result) {
		try {
			service.update(id, request);
		} catch (Exception e) {
			return JsonResponseUtil.error(null, e.getMessage());
		}
		return JsonResponseUtil.success(null);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<BaseResponse<Object>> delete(@PathVariable Long id) {
		service.deleteById(id);
		return JsonResponseUtil.success(null);
	}
}
