package sada.sadamall.oauth.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import sada.sadamall.oauth.repository.Oauth2AuthorizationRequestBasedOnCookieRepository;
import sada.sadamall.utils.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static sada.sadamall.oauth.repository.Oauth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Oauth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        exception.printStackTrace();

        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        String targetUrl = getTargetUrlFrom(request, exception);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String getTargetUrlFrom(HttpServletRequest request, AuthenticationException exception) {
        String redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("/");
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();
        return targetUrl;
    }
}
