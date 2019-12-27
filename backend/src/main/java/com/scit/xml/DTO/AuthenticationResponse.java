package com.scit.xml.DTO;

import java.util.UUID;

public class AuthenticationResponse {

    private UUID id;

    private String role;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(UUID id, String role) {
        this.id = id;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}