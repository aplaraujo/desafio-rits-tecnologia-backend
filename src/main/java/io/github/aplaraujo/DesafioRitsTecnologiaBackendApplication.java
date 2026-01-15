package io.github.aplaraujo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DesafioRitsTecnologiaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioRitsTecnologiaBackendApplication.class, args);
	}

}
