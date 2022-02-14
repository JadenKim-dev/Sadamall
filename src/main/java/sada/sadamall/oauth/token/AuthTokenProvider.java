package sada.sadamall.oauth.token;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
public class AuthTokenProvider {
    private final Key key;

    public static AuthTokenProvider from(String secret) {
        return new AuthTokenProvider(secret);
    }

    private AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthTokenWithoutRole(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthTokenWithRole(String id, String role, Date expiry) {
        return new AuthToken(id, role, expiry, key);
    }
}
