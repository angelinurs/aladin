package com.korutil.server.service;

import com.korutil.server.repository.dao.user.TodoDao;
import com.korutil.server.dto.common.CommonApiResponse;
import com.korutil.server.dto.todo.TodoDto;
import com.korutil.server.dto.todo.TodoSearchDto;
import com.korutil.server.dto.todo.record.TodoResponse;
import com.korutil.server.dto.todo.record.TodoSearchResponse;
import com.korutil.server.service.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoDao todoDao;
    private final JwtTokenService jwtTokenService;
    
    public CommonApiResponse<TodoResponse> create(TodoDto todoDto) {
        todoDto.setUserId(jwtTokenService.getUserIdFromToken());
        TodoDto newTodoDto = todoDao.save(todoDto);

        return CommonApiResponse.success(TodoResponse.fromDto(newTodoDto));
    }
    
    public CommonApiResponse<List<TodoResponse>> getTodos() {
        List<TodoDto> todoDtos = todoDao.findAllByUserId(jwtTokenService.getUserIdFromToken());

        List<TodoResponse> todoResponses = todoDtos.stream().map(TodoResponse::fromDto).toList();

        return CommonApiResponse.success(todoResponses);
    }
    
    public CommonApiResponse<TodoResponse> getTodoById(Long id) {
        Long userId = jwtTokenService.getUserIdFromToken();
        TodoDto todoDto = todoDao.findByIdAndUserId(id, userId);
        return CommonApiResponse.success(TodoResponse.fromDto(todoDto));
    }
    
    public CommonApiResponse<TodoResponse> updateTodo(Long id, TodoDto todoDto) {
        Long userId = jwtTokenService.getUserIdFromToken();
        todoDto.setId(id);
        todoDto.setUserId(userId);
        TodoDto newTodoDto = todoDao.update(todoDto);

        return CommonApiResponse.success(TodoResponse.fromDto(newTodoDto));
    }
    
    public void deleteTodo(Long id) {
        Long userId = jwtTokenService.getUserIdFromToken();

        todoDao.delete(id, userId);
    }
    
    public CommonApiResponse<TodoSearchResponse> search(TodoSearchDto dto, Pageable pageable) {
        Long userId = jwtTokenService.getUserIdFromToken();
        dto.setUserId(userId);
        Page<TodoDto> page = todoDao.search(dto, pageable);
        List<TodoResponse> todos = page.getContent().stream()
                .map(TodoResponse::fromDto)
                .toList();

        TodoSearchResponse todoSearchResponse = new TodoSearchResponse(
                todos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return CommonApiResponse.success(todoSearchResponse);
    }
    
}
