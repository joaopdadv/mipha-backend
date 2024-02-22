package br.com.mipha;

import br.com.mipha.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@SpringBootApplication
public class MiphaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiphaApplication.class, args);
	}

}
