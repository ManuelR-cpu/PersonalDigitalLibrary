package io.github.pafkdunt.pdl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
public class PdlBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdlBackendApplication.class, args);
	}

}
