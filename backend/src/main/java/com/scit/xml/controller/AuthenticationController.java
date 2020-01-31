package com.scit.xml.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
	
//	private final AuthenticationService authenticationService;
//	
//	@Autowired
//	public AuthenticationController(AuthenticationService authenticationService) {
//		this.authenticationService = authenticationService;
//	}
//	
//	
//	@RequestMapping(
//			value = "/signup",
//			method = RequestMethod.POST,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> signUp(@RequestBody SignUpRequest newUser) throws BadRequestException, XMLDBException{	
//		
//		UUID id = authenticationService.signUp(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
//		UriComponents urlLocation = UriComponentsBuilder.newInstance().path("/api/users/{id}").buildAndExpand(id);
//		return ResponseEntity.created(urlLocation.toUri()).build();
//	
//	}
//	
//	@RequestMapping(
//			value = "/signin",
//			method = RequestMethod.POST,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> signIn(@RequestBody SignUpRequest newUser) throws BadRequestException, XMLDBException {
//        return new ResponseEntity<AuthenticationResponse>(authenticationService.signIn(newUser.getUsername(), newUser.getPassword()), HttpStatus.OK);
//    }
//
//
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(
//			value = "/",
//			method = RequestMethod.GET,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> currentUser(){
//		
//		return new ResponseEntity<AuthenticationResponse>(authenticationService.getCurrentUser(), HttpStatus.OK);
//	}
//	
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/signout")
//    public ResponseEntity<?> signout() {
//        SecurityContextHolder.clearContext();
//        return new ResponseEntity<String>(HttpStatus.OK);
//    }
}
