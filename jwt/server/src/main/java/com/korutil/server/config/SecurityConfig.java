package com.korutil.server.config;

import com.korutil.server.component.jwt.CustomAuthenticationProvider;
import com.korutil.server.dto.jwt.CustomClaimNames;
import com.korutil.server.handler.social.CustomOAuthLogoutSuccessHandler;
import com.korutil.server.handler.social.OAuth2LoginFailureHandler;
import com.korutil.server.handler.social.OAuth2SuccessHandler;
import com.korutil.server.service.oauth.UserOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final UserOAuth2Service userOAuth2Service;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuthLogoutSuccessHandler customOAuthLogoutSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                // error endpoint를 열어줘야 함, favicon.ico 추가!
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. CSRF 완전 비활성화 (JWT는 Stateless)
                .csrf(AbstractHttpConfigurer::disable)
                // 2. CORS 설정
//                .csrf(csrf -> csrf.ignoringRequestMatchers("/actuator/**")) // JWT 기반의 Stateless API에서는 CSRF 보호가 필요 없으므로 완전히 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 시큐리티 폼 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // HTTP Basic 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // 3. 세션 관리 Stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation().migrateSession()
                )
                .oauth2Login(oauth ->
                        oauth
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(userOAuth2Service))
                                .successHandler(oAuth2SuccessHandler)
//                                .failureHandler(oAuth2LoginFailureHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customOAuthLogoutSuccessHandler)
                )
                // 4. 요청 권한 설정
                .authorizeHttpRequests(authz -> authz
                                // 공개 경로 (인증 없이 접근 허용)
                                .requestMatchers(
                                        "/api/auth/**",
                                        "/api/valid/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/actuator/**",
                                        "/h2-console/**"
                                ).permitAll()
                                .requestMatchers(
                                        "/users/login",
                                        "/users/signup"
                                ).permitAll()
//                                .requestMatchers("/error","/favicon.ico").permitAll()
                                // 나머지는 인증 필요
                                .anyRequest().authenticated()
                )
                // 5. OAuth2 Resource Server (JWT)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                // 6. H2 콘솔의 세션 문제 해결을 위한 설정
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET","POST","PATCH","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("ROLE_"); // 권한 접두어
        converter.setAuthoritiesClaimName(CustomClaimNames.AUTHORITIES); // 또는 "roles", "authorities"

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtConverter;
    }
}