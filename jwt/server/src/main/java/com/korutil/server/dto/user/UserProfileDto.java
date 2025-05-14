package com.korutil.server.dto.user;

import com.korutil.server.domain.user.UserEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private UserEntity user;
    private Long userId;
    private String name;
    private String phone;
    private LocalDate birth;
    private Boolean activated;

    public static String getJustNumber(String phoneNumber ) {
        return phoneNumber.replaceAll("\\D", "");
    }
}
