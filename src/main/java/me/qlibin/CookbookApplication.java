package me.qlibin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @EnableScheduling is not a Spring Boot annotation, but instead is a Spring Context module annotation.
 * Similar to the @SpringBootApplication and @EnableAutoConfiguration annotations, this is a meta-annotation
 * and internally imports the SchedulingConfiguration via the @Import(SchedulingConfiguration.class) instruction,
 * which can be seen if looked found inside the code for the @EnableScheduling annotation class.
 */
@SpringBootApplication
@EnableScheduling
public class CookbookApplication {

	@Bean
	public StartupRunner schedulerRunner() {
		return new StartupRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(CookbookApplication.class, args);
	}
}
