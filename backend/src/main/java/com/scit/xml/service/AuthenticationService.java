package com.scit.xml.service;

import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.xmldb.api.base.XMLDBException;

import com.scit.xml.domain.User;
import com.scit.xml.domain.constants.DomainConstants;
import com.scit.xml.dto.SignUpRequest;
import com.scit.xml.exception.UndefinedNamespacePrefixMapper;
import com.scit.xml.repository.UserRepository;

@Service
public class AuthenticationService{
	

	private final UserRepository userRepository;
	
	@Autowired
	public AuthenticationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
	public UUID save(SignUpRequest user) throws XMLDBException, JAXBException, UndefinedNamespacePrefixMapper {
		User newUser = new User( user.getUsername(),  user.getPassword(),  user.getEmail(), DomainConstants.AUTHOR);
		return userRepository.save(newUser);
	}
	
	public User findById(UUID id) throws JAXBException, XMLDBException {
		return userRepository.findById(id);
	}

//	
//    public User getLoggedUser() throws Exception {
//        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        try {
//            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
//            String username = user.getUsername();
//            return userRepository.findByUniqueProperty("username", username);
//        } catch (Exception e) {
//			throw new NoSuchElementException("Logged user doesnt exist.");
//        }
//    }


}
