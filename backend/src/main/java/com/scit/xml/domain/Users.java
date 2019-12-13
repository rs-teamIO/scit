package com.scit.xml.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "user"
})
@XmlRootElement(name = "users")
public class Users {

    @XmlElement(required = true)
	protected List<User> user;
	
    public List<User> getUser() {
        if (user == null) {
        	user = new ArrayList<User>();
        }
        return this.user;
    }
    
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Users:");
		for (User u : getUser()) {
			builder.append("\n\tId: " + u.getId());
			builder.append("\n\tUsername: " + u.getUsername());
			builder.append(" [" + u.getRole().toUpperCase()+"]");
			builder.append("\n\tPassword: " + u.getPassword());
			builder.append("\n\tEmail: " + u.getEmail());
			builder.append("\n");
		}
		return builder.toString();
	}
    
}
