package com.scit.xml.repository;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.scit.xml.domain.User;
import com.scit.xml.repository.base.BaseRepository;
import com.scit.xml.repository.base.utility.DatabaseConstants;
import com.scit.xml.repository.base.utility.ExistConnection;

@Repository
public class UserRepository extends BaseRepository<User> {

	@Autowired
	public UserRepository(ExistConnection existConnection) throws IOException, JAXBException {
		super(existConnection);
		this.collectionId = DatabaseConstants.USERS_COLLECTION_ID;
	}
	

}
