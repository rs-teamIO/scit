package com.scit.xml.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.xml.xpath.XPathConstants;

import org.exist.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.scit.xml.DTO.AuthenticationResponse;
import com.scit.xml.repository.AuthenticationRepository;
import com.scit.xml.repository.base.utility.XPathUtil;


@Service
public class AuthenticationService {

	private final AuthenticationRepository authenticationRepository;
	
	
    @Autowired
    public AuthenticationService(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    
    public UUID signUp(String username, String password, String email) throws BadRequestException, XMLDBException {
        
    	if (authenticationRepository.findOne("", "username", username) != null)
            throw new BadRequestException("Username taken!");
    	
    	UUID id = authenticationRepository.saveNewDocument(username, password, email, "author");

//        final Element element = wrapper.getDom().getDocumentElement();
//        final Node node = wrapper.getDom().createElement("role");
//        node.setTextContent(Roles.AUTHOR);
//        element.appendChild(node);
//        wrapper.updateXml();

        return id;
    }
	
    public AuthenticationResponse signIn(String username, String password) throws BadRequestException, XMLDBException {
        
    	XMLResource document = authenticationRepository.signIn(username, password);
    	if (document == null)
            throw new BadRequestException("Invalid username or password!");
    	
    	
        final Node nodeRole = (Node) XPathUtil.evaluate("/document/@role", document.getContentAsDOM(), XPathConstants.NODE);
        final Node nodeId = (Node) XPathUtil.evaluate("/document/@id", document.getContentAsDOM(), XPathConstants.NODE);

        final String role = nodeRole.getTextContent();
        final String id = nodeId.getTextContent();

        
    	final Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        final Authentication authentication = new PreAuthenticatedAuthenticationToken(id, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AuthenticationResponse(UUID.fromString(id), username, role);
    }
    
    public AuthenticationResponse getCurrentUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UUID id = UUID.fromString((String) auth.getPrincipal());
        final Object[] authorities = auth.getAuthorities().toArray();
        final String role = authorities.length > 0 ? authorities[0].toString() : null;

        return new AuthenticationResponse(id, role);
    }
    
    public boolean isAuthenticated() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser");
    }
}
