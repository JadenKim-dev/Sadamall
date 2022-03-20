package sada.sadamall.oauth.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sada.sadamall.utils.CookieUtil;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class Oauth2AuthorizationRequestBasedOnCookieRepositoryTest {

    private Oauth2AuthorizationRequestBasedOnCookieRepository repository;

    @BeforeEach
    public void setup(){
        repository = new Oauth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Test
    public void loadAuthorizationRequest() {
        // given
        OAuth2AuthorizationRequest authRequest = OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri("aaa.com")
                .clientId("1234")
                .build();
        String serializedRequest = CookieUtil.serialize(authRequest);
        Cookie cookie = new Cookie(
                Oauth2AuthorizationRequestBasedOnCookieRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                serializedRequest
        );

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setCookies(cookie);

        // when
        OAuth2AuthorizationRequest resultAuthRequest = repository.loadAuthorizationRequest(mockRequest);

        // then
        assertThat(resultAuthRequest.getAuthorizationRequestUri()).isEqualTo(authRequest.getAuthorizationRequestUri());
        assertThat(resultAuthRequest.getClientId()).isEqualTo(authRequest.getClientId());

    }

    @Test
    public void saveAuthorizationRequest() {
        // given
        OAuth2AuthorizationRequest authRequest = OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri("aaa.com")
                .clientId("1234")
                .build();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // when
        String redirectUri = "bbb.com";
        mockRequest.setParameter(
                Oauth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME,
                redirectUri
        );
        repository.saveAuthorizationRequest(authRequest, mockRequest, mockResponse);

        // then
        Cookie authRequestCookie = mockResponse.getCookie(
                Oauth2AuthorizationRequestBasedOnCookieRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
        );
        assertThat(authRequestCookie.getValue()).isEqualTo(CookieUtil.serialize(authRequest));
        assertThat(authRequestCookie.getMaxAge()).isEqualTo(
                Oauth2AuthorizationRequestBasedOnCookieRepository.COOKIE_EXPIRE_SECONDS
        );

        Cookie redirectUriCookie = mockResponse.getCookie(
                Oauth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME
        );
        assertThat(redirectUriCookie.getValue()).isEqualTo(redirectUri);
        assertThat(redirectUriCookie.getMaxAge()).isEqualTo(
                Oauth2AuthorizationRequestBasedOnCookieRepository.COOKIE_EXPIRE_SECONDS
        );
    }

    @Test
    public void saveAuthorizationRequest_authRequest_is_null() {
        // given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        Cookie authRequestCookie = new Cookie(
                Oauth2AuthorizationRequestBasedOnCookieRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                "request");
        Cookie redirectUriCookie = new Cookie(
                Oauth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME,
                "bbb.com"
        );
        Cookie tokenCookie = new Cookie(
                Oauth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN_COOKIE_NAME,
                "token"
        );
        mockRequest.setCookies(authRequestCookie, redirectUriCookie, tokenCookie);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // when
        repository.saveAuthorizationRequest(null, mockRequest, mockResponse);

        // then
        Cookie resultAuthRequestCookie = mockResponse.getCookie(
                Oauth2AuthorizationRequestBasedOnCookieRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
        );
        assert resultAuthRequestCookie != null;
        assertThat(resultAuthRequestCookie.getValue()).isEqualTo("");

        Cookie resultRedirectUriCookie = mockResponse.getCookie(
                Oauth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME
        );
        assert resultRedirectUriCookie != null;
        assertThat(resultRedirectUriCookie.getValue()).isEqualTo("");

        Cookie resultTokenCookie = mockResponse.getCookie(
                Oauth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN_COOKIE_NAME
        );
        assert resultTokenCookie != null;
        assertThat(resultTokenCookie.getValue()).isEqualTo("");
    }
}
