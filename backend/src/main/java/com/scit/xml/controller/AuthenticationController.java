package com.scit.xml.controller;

import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.xmldb.api.base.XMLDBException;

import com.scit.xml.domain.User;
import com.scit.xml.dto.SignUpRequest;
import com.scit.xml.exception.UndefinedNamespacePrefixMapper;
import com.scit.xml.service.AuthenticationService;

@RestController
@CrossOrigin
@RequestMapping(value="/api/auth")
public class AuthenticationController {
	
	
	private final AuthenticationService authenticationService;
	
	@Autowired
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	
	@RequestMapping(
			value = "/",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signUp(
			@RequestBody SignUpRequest signUpRequest
			) throws XMLDBException, JAXBException, UndefinedNamespacePrefixMapper{
		
		UUID id = authenticationService.save(signUpRequest);
		
		UriComponents urlLocation = UriComponentsBuilder.newInstance().path("/api/auth/{id}").buildAndExpand(id.toString());
		return ResponseEntity.created(urlLocation.toUri()).build();
	}
	

	
	
	@RequestMapping(
			value = "/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> test(@PathVariable("id") UUID id) throws JAXBException, XMLDBException {
		User user = authenticationService.findById(id);
		if(user == null)
			return new ResponseEntity<String>("User with given id doesn't exist.", HttpStatus.NOT_FOUND);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
}
