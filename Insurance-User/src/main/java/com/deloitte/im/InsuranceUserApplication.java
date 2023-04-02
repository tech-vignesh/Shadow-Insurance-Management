package com.deloitte.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories
@EnableSwagger2
public class InsuranceUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceUserApplication.class, args);
	}
	
	
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	


}
