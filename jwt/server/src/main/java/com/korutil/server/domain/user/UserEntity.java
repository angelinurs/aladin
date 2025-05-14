package com.korutil.server.domain.user;

import com.korutil.server.converter.RoleConveter;
import com.korutil.server.dto.user.constant.ROLE;
import com.korutil.server.dto.user.UserDto;
import com.korutil.server.domain.usecase.AdvancedBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "users", schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicUpdate
public class UserEntity extends AdvancedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;  // 로그인용 ID

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 20)
    @Convert(converter = RoleConveter.class)
    private ROLE role;  // USER, ADMIN

    @Builder.Default
    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(role.name().split(","))
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();
    }

    public void updateFromDto(UserDto dto) {

        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.role = dto.getRole();
        this.emailVerified = dto.getEmailVerified();
    }

    public UserDto toDto() {
        return UserDto.builder()
                .id(this.id)
                .username(this.username)
                .email(this.email)
                .role(this.role)
                .emailVerified(this.emailVerified)
                .authorities(this.getAuthorities())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .deletedAt(this.getDeletedAt())
                .activated(this.getActivated())
                .build();
    }

    public static UserEntity fromDto(UserDto dto) {
        return UserEntity.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(dto.getRole())
                .emailVerified(dto.getEmailVerified())
                .build();
    }
}