package sada.sadamall.api.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sada.sadamall.RandomString;
import sada.sadamall.api.entity.user.User;
import sada.sadamall.oauth.entity.ProviderType;
import sada.sadamall.oauth.entity.RoleType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserTest {
    private RandomString randomString;

    @BeforeEach
    public void beforeEach() {
        randomString = new RandomString(64);
    }
    @Test
    public void testConstructor() {
        String oauthId = randomString.nextString();
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .oauthId(oauthId)
                .username("userA")
                .email("aaa@gmail.com")
                .isEmailVerified(true)
                .profileImageUrl("")
                .providerType(ProviderType.GOOGLE)
                .roleType(RoleType.USER)
                .createdAt(now)
                .modifiedAt(now)
                .build();
        assertThat(user.getOauthId()).isEqualTo(oauthId);
        assertThat(user.getUsername()).isEqualTo("userA");
        assertThat(user.getEmail()).isEqualTo("aaa@gmail.com");
        assertThat(user.isEmailVerified()).isTrue();
        assertThat(user.getProfileImageUrl()).isEqualTo("");
        assertThat(user.getProviderType()).isEqualTo(ProviderType.GOOGLE);
        assertThat(user.getRoleType()).isEqualTo(RoleType.USER);
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getModifiedAt()).isEqualTo(now);
        assertThat(user.getPassword()).isEqualTo(User.NO_PASSWORD);
    }
}
