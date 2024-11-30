package ec.edu.espe.kibook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KibookServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(KibookServiceApplication.class, args);
	}
}
