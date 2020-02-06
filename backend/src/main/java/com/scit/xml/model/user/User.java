package com.scit.xml.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
