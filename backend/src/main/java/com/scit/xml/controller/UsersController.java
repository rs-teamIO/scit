package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.RegisterDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestApiEndpoints.USERS)
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final RegisterDtoValidator registerDtoValidator;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE,
                 produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> register(@RequestBody String xml) {
        this.registerDtoValidator.validate(xml);

        final XmlWrapper registerXmlWrapper = new XmlWrapper(xml);
        String id = this.userService.register(registerXmlWrapper);

        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, id));
        return ResponseEntity.ok(responseBody);
    }
}