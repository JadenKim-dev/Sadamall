package sada.sadamall.oauth.token;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sada.sadamall.RandomString;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthTokenTest {
    private AuthTokenProvider authTokenProvider;

    @BeforeEach
    public void beforeEach() {
        RandomString rs = new RandomString(38);
        authTokenProvider = AuthTokenProvider.from(rs.nextString());
    }
    @Test
    public void getTokenClaims() throws Exception {
        //given
        Date now = new Date();
        AuthToken token = authTokenProvider.createAuthToken("1234", new Date(now.getTime() + 10000));

        //when
        Claims claims = token.getTokenClaims();

        //then
        assertThat(claims.getSubject()).isEqualTo("1234");
    }

    @Test
    public void getTokenClaims_만료된_토큰() throws Exception {
        //given
        Date now = new Date();
        AuthToken token = authTokenProvider.createAuthToken("1234", new Date(now.getTime() - 10000));

        //when
        Claims claims = token.getTokenClaims();

        //then
        assertThat(claims).isNull();
    }

    @Test
    public void getExpiredTokenClaims() throws Exception {
        //given
        Date now = new Date();
        AuthToken token = authTokenProvider.createAuthToken("1234", new Date(now.getTime() - 10000));

        //when
        Claims expiredTokenClaims = token.getExpiredTokenClaims();

        //then
        assertThat(expiredTokenClaims.getSubject()).isEqualTo("1234");
    }

    @Test
    public void getExpiredTokenClaims_만료되지_않은_토큰() throws Exception {
        //given
        Date now = new Date();
        AuthToken token = authTokenProvider.createAuthToken("1234", new Date(now.getTime() + 10000));

        //when
        Claims expiredTokenClaims = token.getExpiredTokenClaims();

        //then
        assertThat(expiredTokenClaims).isNull();
    }
}
