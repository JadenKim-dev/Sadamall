package sada.sadamall.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static sada.sadamall.utils.HeaderUtil.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HeaderUtilTest {

    private MockHttpServletRequest request;

    @BeforeEach
    public void beforeEach() {
        request = new MockHttpServletRequest();
    }

    @Test
    public void getAccessToken() {
        //given
        request.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + "result");

        //when
        String accessToken = HeaderUtil.getAccessToken(request);

        //then
        assertThat(accessToken).isEqualTo("result");
    }

    @Test
    public void getAccessTokenWithNoAuthorization() {
        //given

        //when
        String accessToken = HeaderUtil.getAccessToken(request);

        //then
        assertThat(accessToken).isNull();
    }

    @Test
    public void getAccessTokenWithInvalidValue() throws Exception {
        //given
        request.addHeader(HEADER_AUTHORIZATION, "wrong result");

        //when
        String accessToken = HeaderUtil.getAccessToken(request);

        //then
        assertThat(accessToken).isNull();
    }
}
