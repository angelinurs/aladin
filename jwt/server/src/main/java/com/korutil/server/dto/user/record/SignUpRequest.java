package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.UserAddressDto;
import com.korutil.server.dto.user.UserProfileDto;
import com.korutil.server.dto.user.constant.ROLE;
import com.korutil.server.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "이용자 가입 요청")
public record SignUpRequest(

        @Schema(description = "로그인에 사용되는 유저네임", example = "naru")
        String username,  // 이름 또는 닉네임

        @Schema(description = "로그인에 사용되는 유저네임", example = "naru@naru.com")
        String email,  // 이메일

        @Schema(description = "회원 역할 (기본값: USER)", example = "USER", defaultValue = "USER")
        ROLE role,

        @Schema(description = "이름 또는 닉네임", example = "나루")
        String name,

        @Schema(description = "전화번호", example = "01012345678")
        String phone,

        @Schema(description = "생년월일", type = "string", format = "date", example = "1990-01-01")
        LocalDate birth,

        // 주소 관련 필드
        @Schema(description = "주소 관련 필드")
        UserAddressRequest address,

        @Schema(description = "이메일 인증 여부", example = "false", defaultValue = "false")
        Boolean emailVerified,

        @Schema(description = "비밀번호", example = "123")
        String password
) {
    // 생성자에서 기본값 설정 (role 기본값 ROLE.USER)
    public SignUpRequest {
        if (role == null) {
            role = ROLE.USER;
        }
    }

    // DTO로 변환하는 메서드
    public UserDto toUserDto() {
        return UserDto.builder()
                .username(username)
                .email(email)
                .role(role)
                .emailVerified(emailVerified)
                .build();
    }

    public UserAddressDto toUserAddressDto() {
        UserAddressDto dto = address.toDto();
        dto.setSeq(0);
        dto.setActivated(true);

        return dto;
    }

    public UserProfileDto toUserProfileDto() {
        return
                UserProfileDto.builder()
                        .name(name)
                        .phone(phone)
                        .birth(birth)
                        .activated(true)
                        .build();
    }
}
