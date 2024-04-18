package com.tads.webprojeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class WebprojetoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebprojetoApplication.class, args);
	}

}
