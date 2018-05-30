package ru.slavasim.FifaTools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.slavasim"})
public class FifaToolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FifaToolsApplication.class, args);
	}
}
