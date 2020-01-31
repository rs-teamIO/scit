package com.scit.xml.DTO;

public class SignUpRequest {

	private String username;
	
	private String password;
	
	private String email;
	
	public SignUpRequest() {
	}

	public SignUpRequest(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
	
	
	
	
	
	
}
