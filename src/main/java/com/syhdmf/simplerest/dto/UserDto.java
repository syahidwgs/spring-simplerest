package com.syhdmf.simplerest.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
	
	@NotNull
	@Min(value = 8)
	@Max(value = 50)
	private String username;
	
	@NotNull
	@Email
	@Max(value = 75)
	private String email;
	
	@Min(value = 8)
	@Max(value = 20)
	private String password;
	
	@Min(value = 8)
	@Max(value = 20)
	private String password2;
	
	@NotNull
	@Min(value = 8)
	@Max(value = 50)
	private String firstName;
	
	@NotNull
	@Min(value = 8)
	@Max(value = 50)
	private String lastName;
}
