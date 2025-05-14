package com.korutil.server.domain.user;

import com.korutil.server.domain.usecase.AdvancedBaseEntity;
import com.korutil.server.dto.user.UserProfileDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_profiles", schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserProfileEntity extends AdvancedBaseEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(length = 50)
    private String name;

    @Column(length = 20, unique = true)
    private String phone;

    private LocalDate birth;

    public void updateFromDto(UserProfileDto dto) {
        if( !dto.getName().equals(name) ) {
            name = dto.getName();
        }
        if( !dto.getPhone().equals(phone) ) {
            phone = dto.getPhone();
        }
        if( !dto.getBirth().equals(birth) ) {
            birth = dto.getBirth();
        }
    }

    public UserProfileDto toDto() {
        return
                UserProfileDto.builder()
                        .userId(this.userId)
                        .activated(this.getActivated())
                        .name(this.name)
                        .birth(this.birth)
                        .phone(this.phone)
                        .build();
    }

    public static UserProfileEntity fromDto( UserProfileDto dto ) {
        return
                UserProfileEntity.builder()
                        .user(dto.getUser())
                        .name(dto.getName())
                        .phone(dto.getPhone())
                        .birth(dto.getBirth())
                        .build();
    }
}