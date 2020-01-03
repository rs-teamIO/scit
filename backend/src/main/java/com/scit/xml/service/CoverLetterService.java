package com.scit.xml.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.xmldb.api.base.XMLDBException;

import com.scit.xml.repository.CoverLetterRepository;

@Service
public class CoverLetterService {
	
	
	private final CoverLetterRepository coverLetterRepository;

	
	@Autowired
	public CoverLetterService(CoverLetterRepository coverLetterRepository) {
		this.coverLetterRepository = coverLetterRepository;
	}
	
	
	public UUID save(String content) throws XMLDBException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UUID id = UUID.fromString((String) auth.getPrincipal());
        
        return this.coverLetterRepository.save(id, content);
	}
}
