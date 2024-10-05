package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoListRequest;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<TodoSaveResponse> saveTodo(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody TodoSaveRequest todoSaveRequest
    ) {
        return ResponseEntity.ok(todoService.saveTodo(authUser, todoSaveRequest));
    }

    @PostMapping("/todoList")
    public ResponseEntity<Page<TodoResponse>> getTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestBody(required = false) TodoListRequest request,
            @RequestParam(value = "searchStart", required = false, defaultValue = "19990101") @DateTimeFormat(pattern="yyyyMMdd") LocalDate searchStart,
            @RequestParam(value = "searchEnd", required = false, defaultValue = "99991231") @DateTimeFormat(pattern="yyyyMMdd") LocalDate searchEnd
    ) {
        return ResponseEntity.ok(todoService.getTodos(page, size, request, searchStart, searchEnd));
    }

    @PostMapping("/todos/search")
    public ResponseEntity<Page<TodoSearchResponse>> searchTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestBody TodoSearchRequest request
            ) {
        return ResponseEntity.ok(todoService.searchTodos(page, size, request));
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable long todoId) {
        return ResponseEntity.ok(todoService.getTodo(todoId));
    }


}
