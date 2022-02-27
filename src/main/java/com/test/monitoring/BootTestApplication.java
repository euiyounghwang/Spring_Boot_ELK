package com.test.monitoring;


import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.monitoring.service.MonitoringService;

@SpringBootApplication
@EnableScheduling
@ComponentScan
//MapperScan("com.test.monitoring.repository")
public class BootTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootTestApplication.class, args);
	}

}
