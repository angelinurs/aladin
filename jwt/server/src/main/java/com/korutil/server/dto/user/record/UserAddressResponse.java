package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.UserAddressDto;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "사용자 주소 응답 객체")
public record UserAddressResponse(
        @Schema(description = "주소 순번", example = "1")
        Integer seq,

        @Schema(description = "도로명 주소", example = "서울특별시 강남구 테헤란로 123")
        String roadAddress,

        @Schema(description = "지번 주소", example = "서울특별시 강남구 역삼동 123-45")
        String jibunAddress,

        @Schema(description = "상세 주소", example = "101동 202호")
        String detailAddress,

        @Schema(description = "추가 주소 정보", example = "역삼푸르지오아파트")
        String extraAddress,

        @Schema(description = "우편번호", example = "06134")
        String zipcode
) {
    public static UserAddressResponse fromDto(UserAddressDto dto) {
        return new UserAddressResponse(
                dto.getSeq(),
                dto.getRoadAddress(),
                dto.getJibunAddress(),
                dto.getDetailAddress(),
                dto.getExtraAddress(),
                dto.getPostcode()
        );
    }
}
