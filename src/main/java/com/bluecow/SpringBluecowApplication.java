package com.bluecow;

import com.bluecow.entity.Game;
import com.bluecow.entity.Role;
import com.bluecow.service.GameService;
import com.bluecow.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.bluecow.enums.roleName.ROLE_USER;

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
