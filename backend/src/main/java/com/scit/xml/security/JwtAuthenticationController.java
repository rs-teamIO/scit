package com.scit.xml.security;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
    private final JwtRequestValidator jwtRequestValidator;

    @PostMapping(value = "/api/login", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody String xml) {
        JwtRequest authenticationRequest = jwtRequestValidator.validate(xml);
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final JwtUser userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new BadCredentialsException("Credentials invalid.");
        }
    }
}
