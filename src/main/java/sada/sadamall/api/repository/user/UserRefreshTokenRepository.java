package sada.sadamall.api.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sada.sadamall.api.entity.UserRefreshToken;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    UserRefreshToken findByOauthId(String oauthId);
    UserRefreshToken findByOauthIdAndRefreshToken(String oauthId, String refreshToken);
}
