package serveur.mobile.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import serveur.mobile.config.Config;

@SpringBootApplication
public class Application{

	public static void main(String[] args) {

		SpringApplication.run(Config.class, args);
	}
}
