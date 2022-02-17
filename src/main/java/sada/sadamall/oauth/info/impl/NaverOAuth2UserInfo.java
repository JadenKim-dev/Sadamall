package sada.sadamall.oauth.info.impl;

import sada.sadamall.oauth.info.OAuth2UserInfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    public static final String RESPONSE = "response";
    public static final String ID = "id";
    public static final String NAME = "nickname";
    public static final String EMAIL = "email";
    public static final String IMAGE_URL = "profile_image";

    private final Map<String, Object> response;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        response = (Map<String, Object>) attributes.get(RESPONSE);
    }

    @Override
    public String getId() {
        return getInfoOf(ID);
    }

    @Override
    public String getName() {
        return getInfoOf(NAME);
    }

    @Override
    public String getEmail() {
        return getInfoOf(EMAIL);
    }

    @Override
    public String getImageUrl() {
        return getInfoOf(IMAGE_URL);
    }

    private String getInfoOf(String code) {
        return response == null ? null : (String) response.get(code);
    }
}
