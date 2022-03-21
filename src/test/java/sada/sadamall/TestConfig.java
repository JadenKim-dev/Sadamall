package sada.sadamall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import sada.sadamall.config.properties.AppProperties;
import sada.sadamall.oauth.token.ExpiredTokenProvider;

@TestConfiguration
public class TestConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Bean @Autowired
    public ExpiredTokenProvider expiredTokenProvider(AppProperties appProperties) {
        return new ExpiredTokenProvider(secret, appProperties);
    }
}
