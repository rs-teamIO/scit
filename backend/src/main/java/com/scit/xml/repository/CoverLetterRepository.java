package com.scit.xml.repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.XMLDBException;

import com.scit.xml.repository.base.BaseRepository;
import com.scit.xml.repository.base.utility.DatabaseConstants;
import com.scit.xml.repository.base.utility.ExistConnection;

@Repository
public class CoverLetterRepository extends BaseRepository {
	
	@Autowired
	public CoverLetterRepository(ExistConnection existConnection) {
		super(existConnection);
	}
	
	public UUID save(UUID userId, String content) throws XMLDBException {
		return super.insert(userId, DatabaseConstants.COVER_LETTER, content);
	}
	
}
