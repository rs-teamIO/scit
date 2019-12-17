package com.scit.xml.repository.base.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.scit.xml.repository.base.BaseRepository;

@Component
public class DataInitializer {


    @Autowired
    private BaseRepository repo;

    @EventListener(ApplicationReadyEvent.class)
    private void saveXmlFiles() {
        try {
        	
        	//repo.test();
        	repo.findOne(DatabaseConstants.PAPERS, "@id", "66aa2262-10ed-42ae-bd52-ad713041f0c2");
        	repo.findOne(DatabaseConstants.COVER_LETTERS,"@id", "8d90cc23-b9b2-4ca4-8659-96d3307075ff");
        	repo.findOne(DatabaseConstants.REVIEWS, "@id", "95970644-3f54-4916-8a88-a90009f9bf19");
        	
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}