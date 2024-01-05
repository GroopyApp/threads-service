package app.groopy.threadsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ThreadsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ThreadsServiceApplication.class, args);
	}
}
