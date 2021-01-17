package com.cm.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConferenceManagerApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConferenceManagerApplication.class);

	public static void main(String[] args) 
	{
		LOGGER.info("in main page ... ");
		SpringApplication.run(ConferenceManagerApplication.class, args);
	}
	

}
