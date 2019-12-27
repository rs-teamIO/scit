package com.scit.xml.controller;

import java.util.UUID;

import org.exist.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.xmldb.api.base.XMLDBException;

import com.scit.xml.DTO.AuthenticationResponse;
import com.scit.xml.DTO.SignUpRequest;
import com.scit.xml.service.AuthenticationService;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	@Autowired
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	
	@RequestMapping(
			value = "/signup",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signUp(@RequestBody SignUpRequest newUser) throws BadRequestException, XMLDBException{	
		
		UUID id = authenticationService.signUp(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
		UriComponents urlLocation = UriComponentsBuilder.newInstance().path("/api/users/{id}").buildAndExpand(id);
		return ResponseEntity.created(urlLocation.toUri()).build();
	
	}
	
	@RequestMapping(
			value = "/signin",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signIn(@RequestBody SignUpRequest newUser) throws BadRequestException, XMLDBException {
        return new ResponseEntity<AuthenticationResponse>(authenticationService.signIn(newUser.getUsername(), newUser.getPassword()), HttpStatus.OK);
    }


	@RequestMapping(
			value = "/",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> currentUser(){
		
		return new ResponseEntity<AuthenticationResponse>(authenticationService.getCurrentUser(), HttpStatus.OK);
	}
}
