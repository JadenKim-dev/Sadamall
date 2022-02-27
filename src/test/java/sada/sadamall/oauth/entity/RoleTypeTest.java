package sada.sadamall.oauth.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoleTypeTest {
    @Test
    public void of() throws Exception {
        RoleType roleUser = RoleType.of("ROLE_USER");
        RoleType roleAdmin = RoleType.of("ROLE_ADMIN");

        assertThat(roleUser).isEqualTo(RoleType.USER);
        assertThat(roleAdmin).isEqualTo(RoleType.ADMIN);
    }

    @Test
    public void of_잘못된_코드() throws Exception {
        RoleType roleType = RoleType.of("customer");

        assertThat(roleType).isEqualTo(RoleType.GUEST);
    }
}
