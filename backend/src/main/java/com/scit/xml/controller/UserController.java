package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(RestApiEndpoints.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = { APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE }, params = { RestApiRequestParameters.ID })
    public ResponseEntity<String> findById(@RequestParam(RestApiRequestParameters.ID) String id) {
        String userXml = this.userService.findById(id);
        return ResponseEntity.ok(userXml);
    }

    @GetMapping(value = "/me", produces = { APPLICATION_XML_VALUE })
    public ResponseEntity<String> me() {
        String userXml = userService.findById(JwtTokenDetailsUtil.getCurrentUserId());
        return ResponseEntity.ok(userXml);
    }
}
