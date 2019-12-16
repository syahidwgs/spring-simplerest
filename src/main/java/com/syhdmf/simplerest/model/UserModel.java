package com.syhdmf.simplerest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.syhdmf.simplerest.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserModel extends BaseModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	
	@Column(length = 50, nullable = false, unique = true)
	private String username;
	
	@Column(length = 75, nullable = false, unique = true)
	private String email;
	
	private String password;
	
	@Column(length = 50)
	private String firstName;
	
	@Column(length = 50)
	private String lastName;
	
	public UserModel(UserDto request) {
		setFromDto(request);
	}
	
	public void setFromDto(UserDto request) {
		this.username = request.getUsername();
		this.email = request.getEmail();
		this.password = request.getPassword() != null ? request.getPassword() : this.password;
		this.firstName = request.getFirstName();
		this.lastName = request.getLastName();
	}
}
