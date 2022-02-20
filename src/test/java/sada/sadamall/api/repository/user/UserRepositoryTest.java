package sada.sadamall.api.repository.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import sada.sadamall.api.entity.User;
import sada.sadamall.oauth.entity.ProviderType;
import sada.sadamall.oauth.entity.RoleType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired UserRepository repository;
    User user;

    @BeforeEach
    public void beforeEach() {
        user = User.builder()
                .oauthId("123")
                .username("userA")
                .email("aaa@gmail.com")
                .isEmailVerified(true)
                .profileImageUrl("")
                .providerType(ProviderType.GOOGLE)
                .roleType(RoleType.USER)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @Transactional
    public void save() throws Exception {
        //given

        //when
        User savedUser = repository.save(user);

        //then
        assertThat(user).isEqualTo(savedUser);
    }

    @Test
    @Transactional
    public void findByOauthId() throws Exception {
        //given

        //when
        repository.save(user);

        //then
        assertThat(user).isEqualTo(repository.findByOauthId("123"));
    }
}
