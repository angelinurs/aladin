package com.korutil.server.dto.jwt;

import com.korutil.server.util.RequestContext;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenDto {

    private Long userId;
    private String username;
    private String userAgent;
    private String secretKey;
    private String clientIp;
    private String refreshToken;
    private Boolean activated;
    private LocalDateTime refreshTokenCreatedAt;
    private LocalDateTime refreshTokenUpdatedAt;
    private String accessToken;
    private UserDetails userDetails;

    public boolean isEqualRefreshToken( String refreshToken ) {
        return this.getRefreshToken().equals(refreshToken);
    }

    public void updateToken( String accessToken, String refreshToken) {
        this.setAccessToken(accessToken);
        this.setRefreshToken(refreshToken);
    }

    public void deActivate() {
        this.setActivated(false);
    }

    public void activate() {
        this.setActivated(true);
    }

    public JwtTokenResponse toResponse() {
        return
                new JwtTokenResponse( this.getAccessToken(), this.getRefreshToken());
    }

    public static JwtTokenDto from( String username, String secretKey, String userAgent, String accessToken, String refreshToken) {
        return JwtTokenDto.builder()
                .username(username)
                .secretKey(secretKey)
                .userAgent(userAgent)
                .clientIp(RequestContext.getClientIp())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .activated(true)
                .build();
    }

    public static String extractUsernameFromEmail( String email ) {
        return email.substring(0, email.indexOf("@"));
    }
}
