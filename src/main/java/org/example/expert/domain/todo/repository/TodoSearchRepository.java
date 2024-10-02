package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;

public interface TodoSearchRepository {

    Todo findByIdFromQueryDsl(long todoId);
}
