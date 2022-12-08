package com.dreamtown.onasistownhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dreamtown.onasistownhouse.config.PasswordEncode;

@SpringBootApplication
public class OnasistownhouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnasistownhouseApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncode();
	}
}
