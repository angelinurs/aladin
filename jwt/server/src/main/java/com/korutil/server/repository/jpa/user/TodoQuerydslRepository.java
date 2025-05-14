package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.TodoEntity;
import com.korutil.server.dto.todo.TodoSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoQuerydslRepository {
    Page<TodoEntity> searchTodos(TodoSearchDto dto, Pageable pageable);
}
