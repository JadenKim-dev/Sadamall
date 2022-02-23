package sada.sadamall.oauth.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sada.sadamall.api.entity.user.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserPrincipalTest {
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
    public void fromWith1Args() {
        //given

        //when
        UserPrincipal principal = UserPrincipal.from(user);

        //then
        assertThat(principal.getUsername()).isEqualTo("123");
    }

    @Test
    public void fromWith2Args() {
        //given
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("hobby", "soccer");

        //when
        UserPrincipal principal = UserPrincipal.from(user, attributes);

        //then
        assertThat(principal.getUsername()).isEqualTo("123");
        assertThat(principal.getAttributes()).isEqualTo(attributes);
    }
}
