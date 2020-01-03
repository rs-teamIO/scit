package com.scit.xml.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.xpath.XPathConstants;

import org.exist.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.scit.xml.DTO.DocumentResponse;
import com.scit.xml.repository.PaperRepository;
import com.scit.xml.utility.XPathUtil;

@Service
public class PaperService {

	private final PaperRepository paperRepository;

	
	@Autowired
	public PaperService(PaperRepository paperRepository) {
		this.paperRepository = paperRepository;
	}
	
	
	public UUID save(String content) throws XMLDBException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UUID id = UUID.fromString((String) auth.getPrincipal());
        
        return this.paperRepository.save(id, content);
	}
	
	
	public List<DocumentResponse> findAll() throws XMLDBException, BadRequestException {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final UUID id = UUID.fromString((String) auth.getPrincipal());
		
		List<XMLResource> xmlList = paperRepository.findAll(id);
		List<DocumentResponse> respList = new ArrayList<DocumentResponse>();
		
		for(XMLResource doc : xmlList) {
			
	        final Node nodeId = (Node) XPathUtil.evaluate("/d:paper/@id", doc.getContentAsDOM(), XPathConstants.NODE);
	        //final Node nodeStatus = (Node) XPathUtil.evaluate("/d:paper/@status", doc.getContentAsDOM(), XPathConstants.NODE);
	        final Node nodeTitle = (Node) XPathUtil.evaluate("/d:paper/d:title", doc.getContentAsDOM(), XPathConstants.NODE);
	        
	        String docId = nodeId.getTextContent();
 	        String type = "paper";
 	        String status = "someStatus";//nodeStatus.getTextContent();
 	        String title = nodeTitle.getTextContent();
 	        
	        DocumentResponse response = new DocumentResponse(docId, type, status, title);

	        respList.add(response);
		}
		
		return respList;
	}
}
