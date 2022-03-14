package sada.sadamall.oauth.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sada.sadamall.api.entity.user.User;
import sada.sadamall.api.repository.user.UserRepository;
import sada.sadamall.oauth.entity.ProviderType;
import sada.sadamall.oauth.entity.RoleType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    @BeforeEach
    public void beforeEach() {
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    public void loadUserByUsername() throws Exception {
        User user = User.builder()
                .oauthId("1")
                .username("userA")
                .email("aaa@gmail.com")
                .isEmailVerified(true)
                .profileImageUrl("")
                .providerType(ProviderType.GOOGLE)
                .roleType(RoleType.USER)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        given(userRepository.findByOauthId("1")).willReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("1");

        Assertions.assertNotNull(userDetails);
        assertThat(userDetails.getUsername()).isEqualTo("1");
    }

    @Test
    public void loadUserByUsername_wrong_id() throws Exception {
        given(userRepository.findByOauthId("xxx")).willReturn(null);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("xxx"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

}
