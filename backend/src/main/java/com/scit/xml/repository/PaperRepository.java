package com.scit.xml.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.scit.xml.repository.base.BaseRepository;
import com.scit.xml.repository.base.utility.DatabaseConstants;
import com.scit.xml.repository.base.utility.ExistConnection;

@Repository
public class PaperRepository extends BaseRepository {
	
	@Autowired
	public PaperRepository(ExistConnection existConnection) {
		super(existConnection);
	}
	
	
	public UUID save(UUID userId, String content) throws XMLDBException {
		return super.insert(userId, DatabaseConstants.PAPER, content);
	}
	
	public List<XMLResource> findAll(UUID userId) throws XMLDBException {
		return super.findAll(userId, DatabaseConstants.PAPER);
	}
	
	
}
