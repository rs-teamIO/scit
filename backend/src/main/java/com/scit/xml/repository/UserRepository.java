package com.scit.xml.repository;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Repository;

import com.scit.xml.domain.Users;
import com.scit.xml.repository.base.BaseRepository;
import com.scit.xml.repository.base.utils.DatabaseConstants;
import com.scit.xml.repository.base.utils.ExistConnection;

@Repository
public class UserRepository extends BaseRepository<Users> {

	public UserRepository(ExistConnection existConnection) throws IOException, JAXBException {
		super(existConnection);
		this.documentId = DatabaseConstants.USERS_DOCUMENT_ID;
		this.collectionId = DatabaseConstants.USERS_COLLECTION_ID;
	}
	

}
