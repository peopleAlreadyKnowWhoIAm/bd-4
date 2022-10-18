package ua.iot.labs.jdbcProjects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.AllArgsConstructor;
import ua.iot.labs.jdbcProjects.view.ViewController;

@SpringBootApplication
@AllArgsConstructor
public class Application implements CommandLineRunner {

	final ViewController view;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		view.run();
	}

}
