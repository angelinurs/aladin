package com.korutil.server.oauth;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;
    protected String nameAttributeKey;

    protected OAuth2UserInfo(Map<String, Object> attributes, String nameAttributeKey ) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

    public abstract String getId();
    public abstract String getNickname();
    public abstract String getEmail();
    public abstract String getImageUrl();
}
