package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.RegisterDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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

        UriComponents urlLocation = UriComponentsBuilder.newInstance()
                .path(RestApiEndpoints.USER)
                .query(RestApiRequestParameters.ID + "={id}")
                .buildAndExpand(id);

        return ResponseEntity.created(urlLocation.toUri()).build();
    }

    @GetMapping(value = RestApiEndpoints.AUTHORS,
                produces = { MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity getAllAuthors() {
        String authors = this.userService.getAllAuthors();
        return ResponseEntity.ok(XmlResponseUtils.toXmlString(new XmlResponse("users", authors)));
    }
}
