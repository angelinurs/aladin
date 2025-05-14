package com.korutil.server.domain.user;

import com.korutil.server.dto.user.UserCredentialsDto;
import com.korutil.server.domain.usecase.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_password_history", schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserPasswordHistoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // 사용자 ID (users 테이블의 외래키)

    @Column(nullable = false)
    private String password;  // 이전 비밀번호 (해시된 비밀번호)

    @Column(nullable = false)
    private LocalDateTime changedAt;  // 비밀번호 변경 일시

    @PrePersist
    public void prePersist() {
        this.changedAt = LocalDateTime.now();  // 비밀번호 변경 시 자동으로 현재 시간 기록
    }

    public static UserPasswordHistoryEntity fromDto(UserCredentialsDto dto) {
        return
                UserPasswordHistoryEntity.builder()
                        .userId(dto.getUserId())
                        .password(dto.getPassword())
                        .build();
    }
}