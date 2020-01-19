package com.scit.xml.dto;

import com.scit.xml.model.user.Person;
import com.scit.xml.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    private Person person;
    private User user;
}
