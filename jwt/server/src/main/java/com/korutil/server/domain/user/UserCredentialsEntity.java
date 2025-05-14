package com.korutil.server.domain.user;

import com.korutil.server.dto.user.UserCredentialsDto;
import com.korutil.server.domain.usecase.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "user_credentials", schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicUpdate
public class UserCredentialsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // 사용자 ID (users 테이블의 외래키)

    @Column(nullable = false)
    private String password;  // 해시된 비밀번호

    public void setPassword(String password) {
        this.password = password;
    }

    public static UserCredentialsEntity fromDto( UserCredentialsDto dto ) {
        return
                UserCredentialsEntity.builder()
                        .userId(dto.getUserId())
                        .password(dto.getPassword())
                        .build();
    }

    public UserCredentialsDto toDto() {
        return
                UserCredentialsDto.builder()
                        .userId(this.userId)
                        .password(this.password)
                        .build();
    }
}
