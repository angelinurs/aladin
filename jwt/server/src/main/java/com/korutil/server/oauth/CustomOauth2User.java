package com.korutil.server.oauth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOauth2User extends DefaultOAuth2User {
    private final String providerUserId;
    private final OAuthProvider oAuthProvider;
    private final OAuthAttributes oAuthAttributes;
    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOauth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String providerUserId, OAuthProvider oAuthProvider, OAuthAttributes oAuthAttributes) {
        super(authorities, attributes, nameAttributeKey);
        this.providerUserId = providerUserId;
        this.oAuthProvider = oAuthProvider;
        this.oAuthAttributes = oAuthAttributes;
    }
}
