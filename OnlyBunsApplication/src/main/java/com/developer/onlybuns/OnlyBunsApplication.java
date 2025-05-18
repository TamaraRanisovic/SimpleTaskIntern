package com.developer.onlybuns;

import com.developer.onlybuns.repository.AdminSistemRepository;
import com.developer.onlybuns.repository.RegistrovaniKorisnikRepository;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class OnlyBunsApplication {


	@Autowired
	private AdminSistemRepository adminSistemRepository;

	@Autowired
	private RegistrovaniKorisnikRepository registrovaniKorisnikRepository;


	public static void main(String[] args) {
		SpringApplication.run(OnlyBunsApplication.class, args);
	}


	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		return connectionFactory;
	}

}
