package com.korutil.server.controller;

import com.korutil.server.dto.common.CommonApiResponse;
import com.korutil.server.dto.todo.record.*;
import com.korutil.server.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    @Operation(summary = "Todo 저장", description = "Todo 저장 (Authorization 헤더 필요)")
    public CommonApiResponse<TodoResponse> create(@RequestBody TodoRequest request) {
        return todoService.create(request.toDto());
    }

    @GetMapping
    @Operation(summary = "Todo 전체 가져오기", description = "Todo 전체 가져오기 (Authorization 헤더 필요)")
    public CommonApiResponse<List<TodoResponse>> getTodos() {
        return todoService.getTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "해당 Todo 가져오기", description = "해당 Todo 가져오기 (Authorization 헤더 필요)")
    public CommonApiResponse<TodoResponse> getTodosById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "해당 Todo 수정", description = "해당 Todo 수정 (Authorization 헤더 필요)")
    public CommonApiResponse<TodoResponse> updateTodo(@PathVariable Long id, @RequestBody TodoRequest request) {
        return todoService.updateTodo(id, request.toDto());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "해당 Todo 삭제", description = "해당 Todo 삭제 (Authorization 헤더 필요)")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Todo 검색", description = "Todo 검색 정보 반환 (Authorization 헤더 필요)")
    public CommonApiResponse<TodoSearchResponse> searchTodo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sort,
            @RequestParam(defaultValue = "desc")
            @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE)  String direction,
            @ModelAttribute TodoSearchRequest request) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("asc")? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        return todoService.search(request.toDto(), pageable);
    }
}
