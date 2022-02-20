package sada.sadamall.api.repository.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import sada.sadamall.api.entity.UserRefreshToken;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRefreshTokenRepositoryTest {

    @Autowired private UserRefreshTokenRepository repository;

    @Test
    @Transactional
    void save() {
        // given
        UserRefreshToken token = UserRefreshToken.of("123", "tokenA");

        // when
        UserRefreshToken savedToken = repository.save(token);

        // then
        assertThat(token).isEqualTo(savedToken);
    }

    @Test
    @Transactional
    public void findByOauthId() {
        //given
        UserRefreshToken token = UserRefreshToken.of("123", "tokenA");

        //when
        repository.save(token);

        //then
        assertThat(token).isEqualTo(repository.findByOauthId("123"));
    }

    @Test
    @Transactional
    public void findByOauthIdAndRefreshToken() {
        //given
        UserRefreshToken token = UserRefreshToken.of("123", "tokenA");

        //when
        repository.save(token);

        //then
        assertThat(token).isEqualTo(repository.findByOauthIdAndRefreshToken("123", "tokenA"));
    }
}
