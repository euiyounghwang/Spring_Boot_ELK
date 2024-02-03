package com.test.monitoring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan
//MapperScan("com.test.monitoring.repository")
public class BootTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootTestApplication.class, args);
	}

}
