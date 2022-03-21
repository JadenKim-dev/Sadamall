package sada.sadamall.oauth.token;

import io.jsonwebtoken.security.Keys;
import sada.sadamall.config.properties.AppProperties;

import javax.crypto.SecretKey;
import java.util.Date;

public class ExpiredTokenProvider{
    private final SecretKey key;
    AppProperties appProperties;

    public ExpiredTokenProvider(String secret, AppProperties appProperties) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.appProperties = appProperties;
    }

    public AuthToken createExpiredToken(String id) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() - 10000);
        return new AuthToken(id, expiry, key);
    }
}
