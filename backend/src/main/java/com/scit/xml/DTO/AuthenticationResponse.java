package com.scit.xml.DTO;

import java.util.UUID;

public class AuthenticationResponse {

    private UUID id;

    private String username;
    
    private String role;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(UUID id, String username,String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public AuthenticationResponse(UUID id, String role) {
        this.id = id;
        this.role = role;
    }
    public UUID getId() {
        return id;
    }
	public String getUsername() {
		return username;
	}
    public String getRole() {
        return role;
    }



}