package com.orovp.doctools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdfToolsApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(PdfToolsApplication.class);

	public static void main(String[] args) throws Exception {
                
		SpringApplication.run(PdfToolsApplication.class, args);
        
    }
	
}
