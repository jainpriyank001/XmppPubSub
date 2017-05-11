package com.nearbuytools.service.xmpp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.nearbuytools.service" })
public class XMPPService {

	public static void main(String[] args) {
		SpringApplication.run(XMPPService.class, args);
	}
}
