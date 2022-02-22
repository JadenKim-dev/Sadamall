package sada.sadamall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import sada.sadamall.config.properties.AppProperties;
import sada.sadamall.config.properties.CorsProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		AppProperties.class,
		CorsProperties.class
})
public class SadamallApplication {

	public static void main(String[] args) {
		SpringApplication.run(SadamallApplication.class, args);
	}

}
