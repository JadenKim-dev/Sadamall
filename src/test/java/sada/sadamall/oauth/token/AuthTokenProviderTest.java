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
    AuthTokenProvider authTokenProvider;

    @BeforeEach
    public void beforeEach() {
        RandomString rs = new RandomString(38);
        authTokenProvider = AuthTokenProvider.from(rs.nextString());
    }

    @Test
    public void testCreateAuthTokenWithoutRole() {
        long nowTime = new Date().getTime();
        AuthToken token = authTokenProvider.createAuthTokenWithoutRole(
                "1",
                new Date(nowTime + 100000)
        );
        Claims claims = token.getTokenClaims();
        assertThat(token).isInstanceOf(AuthToken.class);
        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.getExpiration().getTime()).isEqualTo((nowTime + 100000)/1000*1000);
    }

    @Test
    public void testCreateAuthTokenWithRole() {
        long nowTime = new Date().getTime();
        AuthToken token = authTokenProvider.createAuthTokenWithRole(
                "1",
                "ROLE_USER",
                new Date(nowTime + 100000)
        );
        Claims claims = token.getTokenClaims();
        assertThat(token).isInstanceOf(AuthToken.class);
        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get(AuthToken.AUTHORITIES_KEY)).isEqualTo("ROLE_USER");
        assertThat(claims.getExpiration().getTime()).isEqualTo((nowTime + 100000)/1000*1000);
    }


}
