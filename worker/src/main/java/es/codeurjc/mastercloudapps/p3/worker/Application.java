package es.codeurjc.mastercloudapps.p3.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		WaitForIt.exec(System.getenv("SPRING_RABBITMQ_HOST"),15672);

		WaitForIt.exec(System.getenv("SPRING_MYSQLDB_HOST"),3306);

		SpringApplication.run(Application.class, args);
	}
}
