package sada.sadamall.oauth.info.impl;

import sada.sadamall.oauth.info.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    public static final String ID = "sub";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String IMAGE_URL = "picture";

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get(ID);
    }

    @Override
    public String getName() {
        return (String) attributes.get(NAME);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(EMAIL);
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get(IMAGE_URL);
    }
}
