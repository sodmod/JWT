package com.Security.JWT;

import com.Security.JWT.Cotrollers.AuthController;
import com.Security.JWT.Cotrollers.TestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class JwtApplication {           //extends SpringBootServletInitializer {

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(
//				AuthController.class,
//				TestController.class
//		);
//	}

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

}
