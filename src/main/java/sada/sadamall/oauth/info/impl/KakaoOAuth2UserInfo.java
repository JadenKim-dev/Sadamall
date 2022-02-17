package sada.sadamall.oauth.info.impl;

import sada.sadamall.oauth.info.OAuth2UserInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    public static final String ID = "id";
    public static final String PROPERTIES = "properties";
    public static final String NAME = "nickname";
    public static final String EMAIL = "account_email";
    public static final String IMAGE_URL = "thumbnail_image";

    private final Map<String, Object> properties;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        properties = (Map<String, Object>) attributes.get(PROPERTIES);
    }

    @Override
    public String getId() {
        return (String) attributes.get(ID);
    }

    @Override
    public String getName() {
        return properties == null ? null : (String) properties.get(NAME);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(EMAIL);
    }

    @Override
    public String getImageUrl() {
        return properties == null ? null : (String) properties.get(IMAGE_URL);
    }
}
