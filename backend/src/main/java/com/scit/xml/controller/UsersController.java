package com.scit.xml.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.user.User;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.RegisterDtoValidator;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * GET api/v1/users/authors
     *
     * Returns a list of authors with their usernames and e-mails
     */
    @GetMapping(value = RestApiEndpoints.AUTHORS,
                produces = { MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<String> getAllAuthors() {
        List<String> authors = this.userService.getAllAuthors();

        StringBuilder sb = new StringBuilder();
        authors.stream()
                .map(XmlResponseUtils::convertToUserXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.USERS, sb.toString()));

        return ResponseEntity.ok(responseBody);
    }
}