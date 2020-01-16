package com.scit.xml.common.util;

import com.scit.xml.model.Person;
import com.scit.xml.model.User;

public class XmlMapper {

    public static String toXml(User user, Person person) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("<user xmlns=\"http://www.scit.org/schema/user\" isActive=\"%s\" id=\"http://www.scit.org/users/%s\">",
                String.valueOf(user.getIsActive()), user.getId()));
        sb.append(String.format("<user:first_name>%s</user:first_name>", person.getFirstName()));
        sb.append(String.format("<user:last_name>%s</user:last_name>", person.getLastName()));
        sb.append(String.format("<user:username>%s</user:username>", user.getUsername()));
        sb.append(String.format("<user:password>%s</user:password>", user.getPassword()));
        sb.append(String.format("<user:email>%s</user:email>", user.getEmail()));
        sb.append(String.format("<user:role>%s</user:role>", user.getRole().getName()));
        sb.append("</user>");

        return sb.toString();
    }
}
