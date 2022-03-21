package sada.sadamall.oauth.token;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sada.sadamall.TestConfig;
import sada.sadamall.config.properties.AppProperties;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import({TestConfig.class})
public class AuthTokenTest {

    @Autowired private AuthTokenProvider authTokenProvider;
    @Autowired private ExpiredTokenProvider expiredTokenProvider;

    @Test
    public void getTokenClaims() throws Exception {
        //given
        Date now = new Date();
        AuthToken token = authTokenProvider.createRefreshToken("1234");

        //when
        Claims claims = token.getTokenClaims();

        //then
        assertThat(claims.getSubject()).isEqualTo("1234");
    }

    @Test
    public void getTokenClaims_만료된_토큰() throws Exception {
        //given
        AuthToken expiredToken = expiredTokenProvider.createExpiredToken("1");

        //when
        Claims claims = expiredToken.getTokenClaims();

        //then
        assertThat(claims).isNull();
    }

    @Test
    public void getExpiredTokenClaims() throws Exception {
        //given
        AuthToken expiredToken = expiredTokenProvider.createExpiredToken("1");
        //when
        Claims expiredTokenClaims = expiredToken.getExpiredTokenClaims();

        //then
        assertThat(expiredTokenClaims.getSubject()).isEqualTo("1");
    }

    @Test
    public void getExpiredTokenClaims_만료되지_않은_토큰() throws Exception {
        //given
        AuthToken token = authTokenProvider.createRefreshToken("1234");

        //when
        Claims expiredTokenClaims = token.getExpiredTokenClaims();

        //then
        assertThat(expiredTokenClaims).isNull();
    }
}
