package com.bluecow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBluecowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBluecowApplication.class, args);
	}

	/*@Bean
	CommandLineRunner run(GameService gameService) {
		return args -> {
			gameService.saveGame(new Game(
					1L,
					"marcosfuentes691@gmail.com",
					1,
					1,
					null,
					"Omu"
			));
			//rolService.save(new Role(ROLE_USER));
		};
		}*/
}
