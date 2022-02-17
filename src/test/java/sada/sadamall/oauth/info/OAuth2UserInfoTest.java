package sada.sadamall.oauth.info;

import org.junit.jupiter.api.Test;
import sada.sadamall.oauth.entity.ProviderType;
import sada.sadamall.oauth.exception.InvalidProviderTypeException;
import sada.sadamall.oauth.info.impl.GoogleOAuth2UserInfo;
import sada.sadamall.oauth.info.impl.KakaoOAuth2UserInfo;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OAuth2UserInfoTest {
    @Test
    public void testGetOAuth2UserInfo() {
        //given
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(GoogleOAuth2UserInfo.ID, "1");
        attributes.put(GoogleOAuth2UserInfo.NAME, "userA");
        attributes.put(GoogleOAuth2UserInfo.EMAIL, "aaa@gmail.com");
        attributes.put(GoogleOAuth2UserInfo.IMAGE_URL, "aaa.com");

        //when
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(ProviderType.GOOGLE, attributes);

        //then
        assertThat(userInfo).isInstanceOf(GoogleOAuth2UserInfo.class);
        assertThat(userInfo.getId()).isEqualTo("1");
        assertThat(userInfo.getName()).isEqualTo("userA");
        assertThat(userInfo.getEmail()).isEqualTo("aaa@gmail.com");
        assertThat(userInfo.getImageUrl()).isEqualTo("aaa.com");
    }

    @Test
    public void testGetOAuth2UserInfo_잘못된_ProviderType() throws Exception {
        //given
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("ID", "1");

        //when
        //then
        assertThrows(InvalidProviderTypeException.class,
                () -> OAuth2UserInfoFactory.getOAuth2UserInfo(ProviderType.LOCAL, attributes));
    }
}
