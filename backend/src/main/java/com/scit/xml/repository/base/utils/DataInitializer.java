package com.scit.xml.repository.base.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.scit.xml.repository.UserRepository;

@Component
public class DataInitializer {


    @Autowired
    private UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    private void saveXmlFiles() {
        try {

            userRepository.initialize();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
