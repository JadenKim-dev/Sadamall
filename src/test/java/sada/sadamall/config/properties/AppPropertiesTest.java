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
public class AppPropertiesTest {
    @Autowired AppProperties appProperties;
    @Value("${app.auth.tokenSecret}") String tokenSecret;
    @Value("${app.auth.tokenExpiry}") long tokenExpiry;
    @Value("${app.auth.refreshTokenExpiry}") long refreshTokenExpiry;

    @Test
    public void getAuth() {
        assertThat(appProperties.getAuth().getTokenSecret()).isEqualTo(tokenSecret);
        assertThat(appProperties.getAuth().getTokenExpiry()).isEqualTo(tokenExpiry);
        assertThat(appProperties.getAuth().getRefreshTokenExpiry()).isEqualTo(refreshTokenExpiry);
    }

    @Test
    public void getOAuth2() {
        System.out.println(appProperties.getOAuth2().getAuthorizedRedirectUris());
    }
}
