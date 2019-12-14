package com.scit.xml.domain;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "username",
    "password",
    "email"
})
@XmlRootElement(name = "user")
public class User implements IDomain {
	
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "anyURI")
    protected UUID id;
    
	@XmlElement(required = true)
    protected String username;
    
    @XmlElement(required = true)
    protected String password;
    
    @XmlElement(required = true)
    protected String email;
    
    @XmlAttribute(name = "role", required = true)
    protected String role;

    public User() {}
    
	public User(UUID id, String username, String password, String email, String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}
	
	public User(String username, String password, String email, String role) {
		super();
		this.id = UUID.randomUUID();
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("User:");
		builder.append("\n\tId: " + id);
		builder.append("\n\tUsername: " + username);
		builder.append(" [" + role.toUpperCase()+"]");
		builder.append("\n\tPassword: " + password);
		builder.append("\n\tEmail: " + email);
		builder.append("\n");
		return builder.toString();
	}
	
	public UUID getId() {
		return id;
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

	public String getRole() {
		return role;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
    
    
    
}
