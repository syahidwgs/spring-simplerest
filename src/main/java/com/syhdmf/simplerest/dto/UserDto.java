package com.syhdmf.simplerest.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
	@Length(min = 8, max = 50)
	private String username;
	
	@NotNull
	@Email
	@Length(max = 75)
	private String email;

	@Length(min = 7, max = 20)
	private String password;
	
	@Length(min = 7, max = 20)
	private String password2;
	
	@NotNull
	@Length(min = 1, max = 50)
	private String firstName;
	
	@Length(max = 50)
	private String lastName;
}
