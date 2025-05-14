package com.korutil.server.dao.user;

import com.korutil.server.domain.user.TodoEntity;
import com.korutil.server.dto.todo.TodoDto;
import com.korutil.server.dto.todo.TodoSearchDto;
import com.korutil.server.dto.todo.record.TodoSearchRequest;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.repository.jpa.user.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TodoDao {

    private final TodoRepository todoRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TodoDto save(TodoDto dto) {
        TodoEntity entity = todoRepository.save(TodoEntity.fromDto(dto));

        return entity.toDto();
    }

    @Transactional(readOnly = true)
    public List<TodoDto> findAllByUserId(Long userId) {
        return
                todoRepository.findAllByUserId(userId)
                        .stream()
                        .map(TodoEntity::toDto)
                        .toList();
    }

    @Transactional(readOnly = true)
    public TodoDto findByIdAndUserId(Long id, Long userId) {
        return
                todoRepository.findByIdAndUserId(id, userId)
                        .map(TodoEntity::toDto)
                        .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_TODO));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TodoDto update(TodoDto dto) {
        TodoEntity entity = todoRepository.findByIdAndUserId(dto.getId(), dto.getUserId()).orElseThrow(
                () -> new CustomRuntimeException(ErrorCode.NOT_FOUND_TODO));
        entity.updateFromDto(dto);

        return entity.toDto();
    }

    @Transactional
    public void delete(long id, Long userId) {
        TodoEntity entity = todoRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new CustomRuntimeException(ErrorCode.NOT_FOUND_TODO));
        entity.softDelete();
    }

    @Transactional(readOnly = true)
    public Page<TodoDto> search(TodoSearchDto dto, Pageable pageable) {
        return todoRepository.searchTodos(dto, pageable)
                .map(TodoEntity::toDto); // 또는 .map(TodoDto::from)
    }
}
