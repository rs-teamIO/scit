package com.scit.xml.repository.base.utility;

import java.util.UUID;

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
        	
        	repo.test();
//        	repo.findOne(DatabaseConstants.PAPER, "@id", "66aa2262-10ed-42ae-bd52-ad713041f0c2");
//        	repo.findOne(DatabaseConstants.COVER_LETTER,"@id", "8d90cc23-b9b2-4ca4-8659-96d3307075ff");
//        	repo.findOne(DatabaseConstants.REVIEW, "@id", "95970644-3f54-4916-8a88-a90009f9bf19");
//        	repo.findDocumentById(UUID.fromString("35cab21b-d7f5-4164-81e8-f2ad25c30c8b"));
//        	repo.removeById(UUID.fromString("4e3e0a80-4fd3-4976-9742-acdb17d3959b"), DatabaseConstants.PAPER, UUID.fromString("66aa2262-10ed-42ae-bd52-ad713041f0c2"));
        	
//        	String paperExample =
//        			"<paper>\r\n" + 
//        			"    		<title>Some long paper title</title>\r\n" + 
//        			"    		<authors>\r\n" + 
//        			"        		<author>\r\n" + 
//        			"            		<name>Katarina Tukelic</name>\r\n" + 
//        			"           			<affiliation>Fakultet tehnickih nauka</affiliation>\r\n" + 
//        			"        		</author>\r\n" + 
//        			"    		</authors>\r\n" + 
//        			"    		<abstract>\r\n" + 
//        			"        		<content>Minimal character count for abstract is 10</content>\r\n" + 
//        			"        		<keywords></keywords>\r\n" + 
//        			"    		</abstract>\r\n" + 
//        			"    		<section>\r\n" + 
//        			"        		<heading>First section heading</heading>\r\n" + 
//        			"        		<content>\r\n" + 
//        			"            		Content can also be an image <image link=\"www.kaca.com\"/>\r\n" + 
//        			"            		After image you can continue with writing whatever you want. \r\n" + 
//        			"            		You can also put another picture:\r\n" + 
//        			"            		<image link=\"www.drugaslika.com\"/> \r\n" + 
//        			"            		You can also put a reference to literature down below:\r\n" + 
//        			"            		<!--<reference paper:id=\"1\"></reference>-->\r\n" + 
//        			"            		You can add section in this section, but it most be after content\r\n" + 
//        			"        		</content>\r\n" + 
//        			"        		<section>\r\n" + 
//        			"            		<heading>Second smaller section with smaller heading</heading>\r\n" + 
//        			"           		<content></content>\r\n" + 
//        			"        		</section>\r\n" + 
//        			"    		</section>\r\n" + 
//        			"    		<references>\r\n" + 
//        			"        		<!--<reference paper:referenceId=\"1\" paper:crossReferenceId=\"someOtherPaperId\">Some book title and some other stuff</reference>-->\r\n" + 
//        			"    		</references>\r\n" + 
//        			"		</paper>";
//        	repo.insert(UUID.fromString("4e3e0a80-4fd3-4976-9742-acdb17d3959b"), DatabaseConstants.PAPER, paperExample);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}