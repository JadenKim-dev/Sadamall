package sada.sadamall.utils;

import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sada.sadamall.api.entity.auth.AuthReqModel;

import javax.servlet.http.Cookie;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class CookieUtilTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void beforeEach() {

        request = new MockHttpServletRequest();
        request.setCookies(new Cookie("cookie1", "1234"));

        response = new MockHttpServletResponse();
    }

    @Test
    public void getCookie() {
        Cookie cookie = CookieUtil.getCookie(request, "cookie1").orElse(null);

        assert cookie != null;
        assertThat(cookie.getName()).isEqualTo("cookie1");
        assertThat(cookie.getValue()).isEqualTo("1234");
    }

    @Test
    public void getCookieWithInvalidName() {
        Cookie cookie = CookieUtil.getCookie(request, "cookie2").orElse(null);
        assertThat(cookie).isNull();
    }

    @Test
    public void addCookie() {
        CookieUtil.addCookie(response, "cookie2", "987", 10);

        Cookie cookie = response.getCookie("cookie2");

        assert cookie != null;
        assertThat(cookie.getName()).isEqualTo("cookie2");
        assertThat(cookie.getValue()).isEqualTo("987");
        assertThat(cookie.getMaxAge()).isEqualTo(10);
    }

    @Test
    public void deleteCookie() {
        CookieUtil.deleteCookie(request, response, "cookie1");
        Cookie cookie = response.getCookie("cookie1");

        assert cookie != null;
        assertThat(cookie.getValue()).isEqualTo("");
        assertThat(cookie.getPath()).isEqualTo("/");
        assertThat(cookie.getMaxAge()).isEqualTo(0);
    }
    
    @Test
    public void serialize() {
        // given
        SerializableClass origin = new SerializableClass("user", 1234);

        // when
        String serialized = CookieUtil.serialize(origin);

        // then
        Cookie cookie = new Cookie("request", serialized);
        SerializableClass deserialized = CookieUtil.deserialize(cookie, SerializableClass.class);
        assertThat(deserialized.getStrField()).isEqualTo(origin.getStrField());
        assertThat(deserialized.getIntField()).isEqualTo(origin.getIntField());
    }

}
