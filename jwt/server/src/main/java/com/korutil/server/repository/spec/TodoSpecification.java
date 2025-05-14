package com.korutil.server.repository.spec;

import com.korutil.server.domain.user.TodoEntity;
import com.korutil.server.dto.todo.TodoSearchDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TodoSpecification {

    private TodoSpecification() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Specification<TodoEntity> search(TodoSearchDto dto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addKeywordPredicate(dto, root, cb, predicates);
            addCategoryPredicate(dto, root, cb, predicates);
            addCompletionPredicate(dto, root, cb, predicates);
            addDateRangePredicates(dto, root, cb, predicates);
            addAllDayPredicate(dto, root, cb, predicates);
            addActivationPredicate(dto, root, cb, predicates);
            addUserIdPredicate(dto, root, cb, predicates);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // 1. 키워드 검색 조건 (제목 또는 설명)
    private static void addKeywordPredicate(
            TodoSearchDto dto,
            Root<TodoEntity> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        if (StringUtils.hasText(dto.getKeyword())) {
            Predicate titlePredicate = cb.like(
                    root.get("title"),
                    "%" + dto.getKeyword() + "%"
            );
            Predicate descPredicate = cb.like(
                    root.get("description"),
                    "%" + dto.getKeyword() + "%"
            );
            predicates.add(cb.or(titlePredicate, descPredicate));
        }
    }

    // 2. 카테고리 필터링
    private static void addCategoryPredicate(
            TodoSearchDto dto,
            Root<TodoEntity> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        if (StringUtils.hasText(dto.getCategory())) {
            predicates.add(
                    cb.equal(root.get("category"), dto.getCategory())
            );
        }
    }

    // 3. 완료 상태 필터링
    private static void addCompletionPredicate(
            TodoSearchDto dto,
            Root<TodoEntity> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        if (dto.getCompleted() != null) {
            predicates.add(
                    cb.equal(root.get("completed"), dto.getCompleted())
            );
        }
    }

    // 4. 날짜 범위 조건 (시작일/종료일)
    private static void addDateRangePredicates(
            TodoSearchDto dto,
            Root<TodoEntity> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        // 시작일 범위
        if (dto.getStartFrom() != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            root.get("startDate"),
                            dto.getStartFrom()
                    )
            );
        }
        if (dto.getStartTo() != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(
                            root.get("startDate"),
                            dto.getStartTo()
                    )
            );
        }

        // 종료일 범위
        if (dto.getEndFrom() != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            root.get("endDate"),
                            dto.getEndFrom()
                    )
            );
        }
        if (dto.getEndTo() != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(
                            root.get("endDate"),
                            dto.getEndTo()
                    )
            );
        }
    }

    // 5. 종일 이벤트 필터링
    private static void addAllDayPredicate(
            TodoSearchDto dto,
            Root<TodoEntity> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        if (dto.getAllDay() != null) {
            predicates.add(
                    cb.equal(root.get("allDay"), dto.getAllDay())
            );
        }
    }

    // 6. 활성화 여부 필터링
    private static void addActivationPredicate(
            TodoSearchDto dto,
            Root<TodoEntity> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        if (dto.getActivated() != null) {
            predicates.add(
                    cb.equal(root.get("activated"), dto.getActivated())
            );
        }
    }

    // 7. 사용자 ID 필터링
    private static void addUserIdPredicate(
            TodoSearchDto dto,
            Root<TodoEntity> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        if (dto.getUserId() != null) {
            predicates.add(
                    cb.equal(root.get("userId"), dto.getUserId())
            );
        }
    }

}
