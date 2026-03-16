package com.example.studentapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
	
	@NotBlank(message="Name is Required")
	@Pattern(regexp="^[a-zA-Z\\s]+$",message="Name must contain only alphabets")
	private String name;
	
	@NotBlank(message="Email is Required")
	@Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", 
    message = "Please provide a valid email (e.g., name@example.com)")
	private String email;
	
	@NotBlank(message="Password is Required")
	@Size(min=6,message="Password must consist of minimum 6 characters")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).+$", 
    message = "Password must be alphanumeric (contain both letters and numbers)")
	private String password;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
