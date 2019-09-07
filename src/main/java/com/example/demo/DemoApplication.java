package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	/*
		C:\Apps\logstash\7.3.1>.\bin\logstash.bat -f .\config\taboola.conf
	 */

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
