package com.korutil.server.service.oauth;

import com.korutil.server.oauth.CustomOauth2User;
import com.korutil.server.oauth.OAuthProvider;
import com.korutil.server.dto.user.SocialLoginDto;
import com.korutil.server.oauth.OAuthAttributes;
import com.korutil.server.service.UserService;
import com.korutil.server.util.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuthProvider oAuthProvider = OAuthProvider.fromString(registrationId);

        String userNameAttributeName =
                userRequest.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(oAuthProvider, userNameAttributeName, originAttributes);

        SocialLoginDto socialLoginDto = getSocialLoginInfo(oAuthAttributes, oAuthProvider);

        return new CustomOauth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                originAttributes,
                oAuthAttributes.getNameAttributeKey(),
                socialLoginDto.getProviderUserId(),
                socialLoginDto.getProvider(),
                oAuthAttributes
        );
    }

    private SocialLoginDto getSocialLoginInfo(OAuthAttributes oAuthAttributes, OAuthProvider oAuthProvider) {
        return Optional.ofNullable(userService.findByProviderUserIdAndOAuthProvider(oAuthAttributes.getOAuth2UserInfo().getId(), oAuthProvider))
                .orElseGet(() -> userService.createSocialUser(SocialLoginDto.of(oAuthAttributes, oAuthProvider)));
    }
}
