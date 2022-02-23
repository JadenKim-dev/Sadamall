package sada.sadamall.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sada.sadamall.api.entity.user.User;
import sada.sadamall.api.repository.user.UserRepository;
import sada.sadamall.oauth.entity.ProviderType;
import sada.sadamall.oauth.entity.RoleType;
import sada.sadamall.oauth.entity.UserPrincipal;
import sada.sadamall.oauth.exception.OAuth2ProviderMissMatchException;
import sada.sadamall.oauth.info.OAuth2UserInfo;
import sada.sadamall.oauth.info.OAuth2UserInfoFactory;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        try {
            return this.process(userRequest, user);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        ProviderType providerType = ProviderType.valueOf(
                userRequest.getClientRegistration().getRegistrationId().toUpperCase()
        );
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                providerType,
                oauth2User.getAttributes()
        );
        User savedUser = userRepository.findByOauthId(userInfo.getId());

        if (savedUser != null) {
            if (providerType != savedUser.getProviderType()) {
                throw new OAuth2ProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
        }
        return UserPrincipal.from(savedUser, oauth2User.getAttributes());
    }

    private void updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getUsername().equals(userInfo.getName())) {
            user.updateUsername(userInfo.getName());
        }

        if (userInfo.getImageUrl() != null && !user.getProfileImageUrl().equals(userInfo.getImageUrl())) {
            user.updateProfileImageUrl(userInfo.getImageUrl());
        }
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .oauthId(userInfo.getId())
                .username(userInfo.getName())
                .email(userInfo.getEmail())
                .isEmailVerified(true)
                .profileImageUrl(userInfo.getImageUrl())
                .providerType(providerType)
                .roleType(RoleType.USER)
                .createdAt(now)
                .modifiedAt(now)
                .build();
        return userRepository.save(user);
    }
}
