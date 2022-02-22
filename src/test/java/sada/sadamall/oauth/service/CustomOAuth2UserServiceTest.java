package sada.sadamall.oauth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import sada.sadamall.api.entity.User;
import sada.sadamall.api.repository.user.UserRepository;
import sada.sadamall.oauth.entity.ProviderType;
import sada.sadamall.oauth.entity.RoleType;
import sada.sadamall.oauth.exception.OAuth2ProviderMissMatchException;
import sada.sadamall.oauth.info.impl.GoogleOAuth2UserInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomOAuth2UserServiceTest {

    @Autowired UserRepository userRepository;

    private CustomOAuth2UserService service;
    private Method process;
    private OAuth2UserRequest mockRequest;

    @BeforeEach
    public void beforeEach() throws NoSuchMethodException {
        service = new CustomOAuth2UserService(userRepository);

        process = service.getClass().getDeclaredMethod("process", OAuth2UserRequest.class, OAuth2User.class);
        process.setAccessible(true);

        ClientRegistration registration = ClientRegistration
                .withRegistrationId("google")
                .authorizationGrantType(new AuthorizationGrantType(RoleType.USER.getCode()))
                .build();
        mockRequest = mock(OAuth2UserRequest.class);
        when(mockRequest.getClientRegistration()).thenReturn(registration);
    }

    @Test
    public void processCreatingUser() throws Exception {
        //given
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(GoogleOAuth2UserInfo.ID, "123");
        attributes.put(GoogleOAuth2UserInfo.NAME, "userA");
        attributes.put(GoogleOAuth2UserInfo.EMAIL, "aaa@gmail.com");
        attributes.put(GoogleOAuth2UserInfo.IMAGE_URL, "aaa.com");
        OAuth2User mockUser = mock(OAuth2User.class);
        when(mockUser.getAttributes()).thenReturn(attributes);

        //when
        OAuth2User user = (OAuth2User) process.invoke(service, mockRequest, mockUser);

        //then
        assertThat(user.getAttributes()).isEqualTo(attributes);
        assertThat(user.getName()).isEqualTo("123");
        assertThat(user.getAuthorities().iterator().next().getAuthority())
                .isEqualTo(RoleType.USER.getCode());
    }

    @Test
    @Transactional
    public void processUpdatingUser() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .oauthId("123")
                .username("prev_name")
                .email("aaa@google.com")
                .isEmailVerified(true)
                .profileImageUrl("prev_url")
                .providerType(ProviderType.GOOGLE)
                .roleType(RoleType.USER)
                .createdAt(now)
                .modifiedAt(now)
                .build();
        userRepository.save(user);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(GoogleOAuth2UserInfo.ID, "123");
        attributes.put(GoogleOAuth2UserInfo.NAME, "new_name");
        attributes.put(GoogleOAuth2UserInfo.EMAIL, "aaa@google.com");
        attributes.put(GoogleOAuth2UserInfo.IMAGE_URL, "new_url");
        OAuth2User mockUser = mock(OAuth2User.class);
        when(mockUser.getAttributes()).thenReturn(attributes);

        //when
        process.invoke(service, mockRequest, mockUser);

        //then
        User findUser = userRepository.findByOauthId("123");
        assertThat(findUser.getUsername()).isEqualTo("new_name");
        assertThat(findUser.getProfileImageUrl()).isEqualTo("new_url");
    }

    @Test
    public void processWithProviderMissMatch() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .oauthId("123")
                .username("prev_name")
                .email("aaa@google.com")
                .isEmailVerified(true)
                .profileImageUrl("prev_url")
                .providerType(ProviderType.FACEBOOK)
                .roleType(RoleType.USER)
                .createdAt(now)
                .modifiedAt(now)
                .build();
        userRepository.save(user);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(GoogleOAuth2UserInfo.ID, "123");
        attributes.put(GoogleOAuth2UserInfo.NAME, "new_name");
        attributes.put(GoogleOAuth2UserInfo.EMAIL, "aaa@google.com");
        attributes.put(GoogleOAuth2UserInfo.IMAGE_URL, "new_url");
        OAuth2User mockUser = mock(OAuth2User.class);
        when(mockUser.getAttributes()).thenReturn(attributes);

        //when
        InvocationTargetException exception = assertThrows(
                InvocationTargetException.class,
                () -> process.invoke(service, mockRequest, mockUser)
        );

        //then
        assertThat(exception.getCause()).isInstanceOf(OAuth2ProviderMissMatchException.class);
    }
}
