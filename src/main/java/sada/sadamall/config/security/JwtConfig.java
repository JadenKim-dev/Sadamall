package sada.sadamall.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sada.sadamall.config.properties.AppProperties;
import sada.sadamall.oauth.token.AuthTokenProvider;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Bean @Autowired
    public AuthTokenProvider jwtProvider(AppProperties appProperties) {
        return new AuthTokenProvider(secret, appProperties);
    }
}