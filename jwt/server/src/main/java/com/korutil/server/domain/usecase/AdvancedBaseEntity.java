package com.korutil.server.domain.usecase;

import com.korutil.server.util.CustomLocalDateTimeUtils;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreRemove;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@DynamicUpdate
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("deleted_at IS NULL")
public abstract class AdvancedBaseEntity extends BaseEntity {

    @Column(nullable = false)
    protected Boolean activated = true;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;  // NULL이면 삭제 안 됨

    // 소프트 삭제 처리 메서드 (Optional)
    public void softDelete() {
        this.setDeletedAt(CustomLocalDateTimeUtils.getNow());
        this.setActivated(false); // 옵션: 같이 false로 바꿔줘도 좋음
    }

    @PreRemove // 추가: 물리적 삭제 방지
    private void preventPhysicalDelete() {
        throw new UnsupportedOperationException(
                "물리적 삭제 금지. softDelete() 메서드를 사용하세요."
        );
    }
}
