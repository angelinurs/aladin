package com.korutil.server.dto.user;

import com.korutil.server.util.PasswordUtils;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredentialsDto {
    private Long userId;
    private String password;  // 암호화된 비밀번호

    public static UserCredentialsDto newInstance( Long userId, String password ) {
        String saltedPassword = PasswordUtils.generate( password);
        return
                UserCredentialsDto.builder()
                        .userId(userId)
                        .password(saltedPassword)
                        .build();
    }
}
