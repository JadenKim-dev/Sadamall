package sada.sadamall.config.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CorsPropertiesTest {
    @Autowired CorsProperties corsProperties;
    @Value("${cors.allowed-origins}") String allowedOrigins;
    @Value("${cors.allowed-methods}") String allowedMethods;
    @Value("${cors.allowed-headers}") String allowedHeaders;
    @Value("${cors.max-age}") Long maxAge;

    @Test
    public void getter() {
        assertThat(corsProperties.getAllowedOrigins()).isEqualTo(allowedOrigins);
        assertThat(corsProperties.getAllowedMethods()).isEqualTo(allowedMethods);
        assertThat(corsProperties.getAllowedHeaders()).isEqualTo(allowedHeaders);
        assertThat(corsProperties.getMaxAge()).isEqualTo(maxAge);
    }
}
