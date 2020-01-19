package com.scit.xml.service.validator.dto;

import com.scit.xml.common.util.BadRequestUtils;
import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.dto.RegisterDto;
import com.scit.xml.model.user.Person;
import com.scit.xml.model.user.Role;
import com.scit.xml.model.user.User;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class RegisterDtoValidator {

    private final String FIRST_NAME = "/register/person/first_name";
    private final String LAST_NAME = "/register/person/last_name";
    private final String USERNAME = "/register/user/username";
    private final String PASSWORD = "/register/user/password";
    private final String EMAIL = "/register/user/email";
    private final String ROLE = "/register/user/role";

    public RegisterDto validate(String xml) {
        Document document = new XmlWrapper(xml).getDocument();
        RegisterDto registerDto = new RegisterDto();

        extractUser(document, registerDto);
        extractPerson(document, registerDto);

//        switch (registerDto.getUser().getRole()) {
//            case AUTHOR:
//                extractAuthor(document, registerDto);
//                break;
//            case EDITOR:
//                extractEditor(registerDto);
//                break;
//        }

        return registerDto;
    }

    private void extractPerson(Document document, RegisterDto registerDto) {
        String firstName = XmlExtractorUtil.extractStringAndValidateNotBlank(document, FIRST_NAME);
        String lastName = XmlExtractorUtil.extractStringAndValidateNotBlank(document, LAST_NAME);

        registerDto.setPerson(new Person(firstName, lastName));
    }

    private void extractUser(Document document, RegisterDto registerDto) {
        String username = XmlExtractorUtil.extractStringAndValidateNotBlank(document, USERNAME);
        String password = XmlExtractorUtil.extractStringAndValidateNotBlank(document, PASSWORD);
        String email = XmlExtractorUtil.extractStringAndValidateNotBlank(document, EMAIL);
        String roleString = XmlExtractorUtil.extractStringAndValidateNotBlank(document, ROLE);
        Role role = getRoleFromRestAPI(roleString);

        registerDto.setUser(new User(null, username, password, email, role, true));
    }

    private Role getRoleFromRestAPI(String roleString) {
        Role role = Role.getRoleByName(roleString);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(role == null, "Unexpected user role value: '" + roleString + "'");
        return role;
    }
}
