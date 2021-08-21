package com.bluecow;

import com.bluecow.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SpringBluecowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBluecowApplication.class, args);
	}

	@Bean
	CommandLineRunner run(GameRepository gameRepository) {
		return args -> {
			log.info(gameRepository.getFirstByIdIsLessThanAndPlayerOrderByIdDesc(200L, "marcosfuentes691@gmail.com").getHero());
			log.info(gameRepository.getFirstByIdIsLessThanAndPlayerOrderByIdDesc(10L, "marcosfuentes691@gmail.com").getTimestamp().toString());
		};
	}
}
