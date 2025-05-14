package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.QTodoEntity;
import com.korutil.server.domain.user.TodoEntity;
import com.korutil.server.dto.todo.TodoSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class TodoQuerydslRepositoryImpl implements TodoQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TodoEntity> searchTodos(TodoSearchDto dto, Pageable pageable) {
        QTodoEntity todo = QTodoEntity.todoEntity;

        // 1. 동적 쿼리 생성
        JPQLQuery<TodoEntity> query = queryFactory
                .selectFrom(todo)
                .where(buildPredicates(dto, todo));

        // 2. 정렬 및 페이징 적용
        applySortingAndPaging(query, pageable, todo);

        // 3. 페이징 결과 조회
        List<TodoEntity> content = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<TodoEntity>(content, pageable, total);
    }

    private BooleanBuilder buildPredicates(TodoSearchDto dto, QTodoEntity todo) {
        BooleanBuilder builder = new BooleanBuilder();

        // 키워드 검색 (제목/설명)
        if (StringUtils.hasText(dto.getKeyword())) {
            builder.and(todo.title.containsIgnoreCase(dto.getKeyword())
                    .or(todo.description.containsIgnoreCase(dto.getKeyword())));
        }

        // 카테고리
        if (StringUtils.hasText(dto.getCategory())) {
            builder.and(todo.category.eq(dto.getCategory()));
        }

        // 완료 여부
        if (dto.getCompleted() != null) {
            builder.and(todo.completed.eq(dto.getCompleted()));
        }

        // 날짜 범위 조건
        if (dto.getStartFrom() != null) {
            builder.and(todo.startDate.goe(dto.getStartFrom()));
        }
        if (dto.getStartTo() != null) {
            builder.and(todo.startDate.loe(dto.getStartTo()));
        }
        if (dto.getEndFrom() != null) {
            builder.and(todo.endDate.goe(dto.getEndFrom()));
        }
        if (dto.getEndTo() != null) {
            builder.and(todo.endDate.loe(dto.getEndTo()));
        }

        // 종일 이벤트
        if (dto.getAllDay() != null) {
            builder.and(todo.allDay.eq(dto.getAllDay()));
        }

        // 활성화 여부
        if (dto.getActivated() != null) {
            builder.and(todo.activated.eq(dto.getActivated()));
        }

        // 사용자 ID
        if (dto.getUserId() != null) {
            builder.and(todo.userId.eq(dto.getUserId()));
        }

        return builder;
    }

    private void applySortingAndPaging(
            JPQLQuery<TodoEntity> query,
            Pageable pageable,
            QTodoEntity todo
    ) {
        // 정렬 적용
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            for (Sort.Order order : sort) {
                PathBuilder<TodoEntity> pathBuilder = new PathBuilder<>(TodoEntity.class, "todoEntity");
                Path<Comparable> path = pathBuilder.get(order.getProperty(), Comparable.class);
                query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, path));
            }
        }

        // 페이징 적용
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }
}
