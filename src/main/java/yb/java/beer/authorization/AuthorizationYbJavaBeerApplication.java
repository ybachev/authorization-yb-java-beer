package yb.java.beer.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan("yb.java.beer.authorization.spring")
public class AuthorizationYbJavaBeerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationYbJavaBeerApplication.class, args);
	}
}
