package com.bluecow;

import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import com.bluecow.service.GameService;
import com.bluecow.utility.BearerCleaner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;

@SpringBootApplication
@Slf4j
public class SpringBluecowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBluecowApplication.class, args);
	}

	@Bean
	CommandLineRunner run(GameService gameService) {
		return args -> {
			log.info(System.getenv("DATABASE_URL"));
			log.info(System.getenv("SECRET"));
			//Calendar calFrom = Calendar.getInstance();
			//calFrom.set(2021, Calendar.SEPTEMBER,3,1,1,1);
			//Calendar calTo = Calendar.getInstance();
			//calTo.set(2021,Calendar.SEPTEMBER,10,1,1,1);
			//gameService.randomGames(calFrom,calTo,5087);
			//log.info(bearerCleaner.cleanBearer("Bearer asdasdasd"));
			//log.info(gameRepository.getFirstByIdIsLessThanAndPlayerOrderByIdDesc(200L, "marcosfuentes691@gmail.com").getHero());
			//log.info(gameRepository.getFirstByIdIsLessThanAndPlayerOrderByIdDesc(10L, "marcosfuentes691@gmail.com").getTimestamp().toString());
		};
	}
}
