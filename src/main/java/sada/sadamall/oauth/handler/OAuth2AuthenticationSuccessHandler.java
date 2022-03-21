package sada.sadamall.oauth.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import sada.sadamall.api.entity.user.UserRefreshToken;
import sada.sadamall.api.repository.user.UserRefreshTokenRepository;
import sada.sadamall.config.properties.AppProperties;
import sada.sadamall.oauth.entity.ProviderType;
import sada.sadamall.oauth.entity.RoleType;
import sada.sadamall.oauth.info.OAuth2UserInfo;
import sada.sadamall.oauth.info.OAuth2UserInfoFactory;
import sada.sadamall.oauth.repository.Oauth2AuthorizationRequestBasedOnCookieRepository;
import sada.sadamall.oauth.token.AuthToken;
import sada.sadamall.oauth.token.AuthTokenProvider;
import sada.sadamall.utils.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import static sada.sadamall.oauth.repository.Oauth2AuthorizationRequestBasedOnCookieRepository.*;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final Oauth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUri = getRedirectUriFrom(request);

        OidcUser user = (OidcUser) authentication.getPrincipal();
        OAuth2UserInfo userInfo = createUserInfoFrom(authentication, user);
        RoleType roleType = getRoleTypeFrom(user);

        AuthToken accessToken = tokenProvider.createAccessToken(userInfo.getId(), roleType.getCode());

        AuthToken refreshToken = tokenProvider.createRefreshToken(appProperties.getAuth().getTokenSecret());
        saveUserRefreshTokenOf(refreshToken, userInfo);

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken.getToken(), cookieMaxAge);

        return getTargetUriFrom(redirectUri, accessToken);
    }

    private String getRedirectUriFrom(HttpServletRequest request) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        if(!isAuthorizedRedirectUri(targetUrl)) {
            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        return targetUrl;
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        return appProperties.getOAuth2().getAuthorizedRedirectUris()
                .stream()
                .map(URI::create)
                .anyMatch(authorizedUri -> authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                        && authorizedUri.getPort() == clientRedirectUri.getPort());
    }

    private OAuth2UserInfo createUserInfoFrom(Authentication authentication, OidcUser user) {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        ProviderType providerType = ProviderType.valueOf(
                authToken.getAuthorizedClientRegistrationId().toUpperCase()
        );
        return OAuth2UserInfoFactory.getOAuth2UserInfo(
                providerType,
                user.getAttributes()
        );
    }

    private RoleType getRoleTypeFrom(OidcUser user) {

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        return hasAuthority(authorities, RoleType.ADMIN.getCode())
                ? RoleType.ADMIN
                : RoleType.USER;
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        if (authorities == null) {
            return false;
        }
        return authorities.stream()
                .anyMatch(grantedAuthority -> authority.equals(grantedAuthority.getAuthority()));
    }

    private void saveUserRefreshTokenOf(AuthToken refreshToken, OAuth2UserInfo userInfo) {
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByOauthId(userInfo.getId());
        if (userRefreshToken != null) {
            userRefreshToken.updateRefreshToken(refreshToken.getToken());
        } else {
            userRefreshToken = UserRefreshToken.newToken(userInfo.getId(), refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        }
    }

    private String getTargetUriFrom(String redirectUri, AuthToken accessToken) {
        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", accessToken.getToken())
                .build().toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
