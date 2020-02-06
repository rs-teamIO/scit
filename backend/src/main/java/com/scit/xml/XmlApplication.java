package com.scit.xml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class XmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmlApplication.class, args);
	}

}
