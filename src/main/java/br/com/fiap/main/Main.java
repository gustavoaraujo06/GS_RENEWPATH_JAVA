package br.com.fiap.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"br.com.fiap.controller", "br.com.fiap.bo", "br.com.fiap.dao", "br.com.fiap.model"})
public class Main {

	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}