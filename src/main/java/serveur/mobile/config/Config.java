package serveur.mobile.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { 
		"serveur.mobile.metier",
		"serveur.mobile.metierImpl",
		"serveur.mobile.web",
		"serveur.mobile.controls",
		"serveur.mobile.dao",
		"serveur.mobile.dao.impl"})

@EnableAutoConfiguration
public class Config implements CommandLineRunner {

	public static final Logger log = LoggerFactory.getLogger(Config.class);
	@Override
	public void run(String... arg0) throws Exception {

		//Make stuff here if need. 
		//Allow to configure the server at start
	}
}
