package com.scit.xml.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.RegisterDtoValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(RestApiEndpoints.USERS)
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final RegisterDtoValidator registerDtoValidator;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> register(@RequestBody String xml) {
        this.registerDtoValidator.validate(xml);

        final XmlWrapper registerXmlWrapper = new XmlWrapper(xml);
        String id = this.userService.register(registerXmlWrapper);
        
        UriComponents urlLocation = UriComponentsBuilder.newInstance()
        		.path(RestApiEndpoints.USER)
        		.query(RestApiRequestParameters.ID+"={id}")
        		.buildAndExpand(id);
        
		return ResponseEntity.created(urlLocation.toUri()).build();

    }
}