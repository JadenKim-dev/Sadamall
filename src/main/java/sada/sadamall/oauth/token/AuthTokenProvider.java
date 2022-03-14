package sada.sadamall.oauth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import sada.sadamall.oauth.exception.TokenValidFailedException;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
public class AuthTokenProvider {
    private final Key key;

    public static AuthTokenProvider of(String secret) {
        return new AuthTokenProvider(secret);
    }

    private AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthToken(String id, String role, Date expiry) {
        return new AuthToken(id, role, expiry, key);
    }

    public AuthToken convertAuthTokenFrom(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) {
        if(authToken.validate()) {
            Claims claims = authToken.getTokenClaims();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                    claims.get(AuthToken.AUTHORITIES_KEY).toString()
            );
            Collection<? extends SimpleGrantedAuthority> authorities = List.of(authority);
            User principal = new User(claims.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } else {
            throw new TokenValidFailedException();
        }
    }
}
