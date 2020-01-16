package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.dto.RegisterDto;
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

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> register(@RequestBody String xml) {
        RegisterDto registerDto = registerDtoValidator.validate(xml);
        String id = userService.register(registerDto);

        // TODO: Fix
        return ResponseEntity.ok(id);
    }
}
