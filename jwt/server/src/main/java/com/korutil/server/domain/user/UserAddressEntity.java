package com.korutil.server.domain.user;

import com.korutil.server.dto.user.UserAddressDto;
import com.korutil.server.domain.usecase.AdvancedBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "user_address", schema = "member",
        indexes = {
                @Index(name = "idx_user_id", columnList = "user_id"),
                @Index(name = "idx_postcode", columnList = "postcode")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicUpdate
public class UserAddressEntity extends AdvancedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    // 사용자 ID (users 테이블의 외래키)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 우선순위
    @Column(name = "seq", nullable = false, columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer seq = 0;

    // 도로명 주소
    @Column(name = "road_address", nullable = false, length = 255)
    private String roadAddress;

    // 지번 주소 (nullable)
    @Column(name = "jibun_address", length = 255)
    private String jibunAddress;

    // 상세주소 (nullable)
    @Column(name = "detail_address", length = 255)
    private String detailAddress;

    // 참고항목 (nullable)
    @Column(name = "extra_address", length = 255)
    private String extraAddress;

    // 우편번호
    @Column(name = "postcode", nullable = false, length = 20)
    private String postcode;

    public void updateFromDto(UserAddressDto dto) {

        if(!seq.equals(dto.getSeq())) {
            seq = dto.getSeq();
        }

        if (!roadAddress.equals(dto.getRoadAddress())) {
            roadAddress = dto.getRoadAddress();
        }

        if (!jibunAddress.equals(dto.getJibunAddress())) {
            jibunAddress = dto.getJibunAddress();
        }

        if (!detailAddress.equals(dto.getDetailAddress())) {
            detailAddress = dto.getDetailAddress();
        }

        if (!extraAddress.equals(dto.getExtraAddress())) {
            extraAddress = dto.getExtraAddress();
        }

        if (!postcode.equals(dto.getPostcode())) {
            postcode = dto.getPostcode();
        }

        if (getActivated() != dto.getActivated()) {
            setActivated(dto.getActivated());
        }
    }

    public UserAddressDto toDto() {
        return
                UserAddressDto.builder()
                        .id(this.id)
                        .userId(this.userId)
                        .seq(this.seq)
                        .roadAddress(this.roadAddress)
                        .jibunAddress(this.jibunAddress)
                        .detailAddress(this.detailAddress)
                        .extraAddress(this.extraAddress)
                        .postcode(this.postcode)
                        .activated(this.getActivated())
                        .build();
    }

    public static UserAddressEntity fromDto( UserAddressDto dto ) {
        return
                UserAddressEntity.builder()
                        .userId(dto.getUserId())
                        .seq(dto.getSeq())
                        .roadAddress(dto.getRoadAddress())
                        .jibunAddress(dto.getJibunAddress())
                        .detailAddress(dto.getDetailAddress())
                        .extraAddress(dto.getExtraAddress())
                        .postcode(dto.getPostcode())
                        .build();
    }
}