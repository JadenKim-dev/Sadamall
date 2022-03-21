package sada.sadamall.api.controller.auth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sada.sadamall.api.entity.user.UserRefreshToken;
import sada.sadamall.api.entity.auth.AuthReqModel;
import sada.sadamall.api.repository.user.UserRefreshTokenRepository;
import sada.sadamall.common.ApiResponse;
import sada.sadamall.config.properties.AppProperties;
import sada.sadamall.oauth.entity.RoleType;
import sada.sadamall.oauth.entity.UserPrincipal;
import sada.sadamall.oauth.token.AuthToken;
import sada.sadamall.oauth.token.AuthTokenProvider;
import sada.sadamall.utils.CookieUtil;
import sada.sadamall.utils.HeaderUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @PostMapping("/login")
    public ApiResponse<String> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AuthReqModel authReqModel
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authReqModel.getId(),
                        authReqModel.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String role = ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode();
        AuthToken accessToken = tokenProvider.createAccessToken(authReqModel.getId(), role);

        AuthToken refreshToken = tokenProvider.createRefreshToken(appProperties.getAuth().getTokenSecret());
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByOauthId(authReqModel.getId());
        if (userRefreshToken == null) {
            userRefreshToken = UserRefreshToken.newToken(authReqModel.getId(), refreshToken.getToken());
            userRefreshTokenRepository.save(userRefreshToken);
        } else {
            userRefreshToken.updateRefreshToken(refreshToken.getToken());
        }

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return ApiResponse.success("token", accessToken.getToken());
    }

    @GetMapping("/refresh")
    public ApiResponse<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authAccessToken = tokenProvider.convertAuthTokenFrom(accessToken);
        if(!authAccessToken.validate()) {
            return ApiResponse.invalidAccessToken();
        }
        Claims claims = authAccessToken.getExpiredTokenClaims();
        if (claims == null) {
            return ApiResponse.notExpiredTokenYet();
        }

        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse(null);
        AuthToken authRefreshToken = tokenProvider.convertAuthTokenFrom(refreshToken);
        if (authRefreshToken.validate()) {
            return ApiResponse.invalidRefreshToken();
        }

        String userId = claims.getSubject();
        UserRefreshToken userRefreshToken =
                userRefreshTokenRepository.findByOauthIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
            return ApiResponse.invalidRefreshToken();
        }

        RoleType roleType = RoleType.of(claims.get("role", String.class));
        AuthToken newAccessToken = tokenProvider.createAccessToken(userId, roleType.getCode());

        Date now = new Date();
        long valid_time = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        if (valid_time <= THREE_DAYS_MSEC) {
            authRefreshToken = tokenProvider.createRefreshToken(appProperties.getAuth().getTokenSecret());
            userRefreshToken.updateRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, refreshToken, authRefreshToken.getToken(), cookieMaxAge);
        }
        return ApiResponse.success("token", newAccessToken.getToken());
    }

}

