package com.matias.studying.config.security.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MyPasswordEncoder {
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
