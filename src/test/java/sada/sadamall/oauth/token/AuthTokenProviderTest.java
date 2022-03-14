package sada.sadamall.oauth.token;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sada.sadamall.RandomString;
import sada.sadamall.oauth.entity.RoleType;
import sada.sadamall.oauth.exception.TokenValidFailedException;

import java.util.Collection;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthTokenProviderTest {
    AuthTokenProvider authTokenProvider;

    @BeforeEach
    public void beforeEach() {
        RandomString rs = new RandomString(38);
        authTokenProvider = AuthTokenProvider.of(rs.nextString());
    }

    @Test
    public void createAuthTokenWithoutRole() {
        long nowTime = new Date().getTime();
        AuthToken token = authTokenProvider.createAuthToken(
                "1",
                new Date(nowTime + 100000)
        );
        Claims claims = token.getTokenClaims();
        assertThat(token).isInstanceOf(AuthToken.class);
        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.getExpiration().getTime()).isEqualTo((nowTime + 100000)/1000*1000);
    }

    @Test
    public void createAuthTokenWithRole() {
        long nowTime = new Date().getTime();
        AuthToken token = authTokenProvider.createAuthToken(
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

    @Test
    public void convertAuthTokenFrom() throws Exception {
        //given
        String token = "12345";

        //when
        AuthToken authToken = authTokenProvider.convertAuthTokenFrom(token);

        //then
        assertThat(authToken.getToken()).isEqualTo("12345");
    }

    @Test
    public void getAuthentication() {
        long nowTime = new Date().getTime();
        AuthToken authToken = authTokenProvider.createAuthToken(
                "1",
                RoleType.USER.getCode(),
                new Date(nowTime + 100000)
        );
        Authentication authentication = authTokenProvider.getAuthentication(authToken);
        User principal = (User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        GrantedAuthority authority = authorities.iterator().next();
        Object credentials = authentication.getCredentials();

        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(principal.getUsername()).isEqualTo("1");
        assertThat(principal.getPassword()).isEqualTo("");
        assertThat(authority.getAuthority()).isEqualTo(RoleType.USER.getCode());
        assertThat(credentials).isEqualTo(authToken);
    }

    @Test
    public void getAuthentication_만료된_토큰() {
        long nowTime = new Date().getTime();
        AuthToken authToken = authTokenProvider.createAuthToken(
                "1",
                RoleType.USER.getCode(),
                new Date(nowTime - 100000)
        );
        assertThatThrownBy(() -> authTokenProvider.getAuthentication(authToken))
                .isInstanceOf(TokenValidFailedException.class);
    }



}
