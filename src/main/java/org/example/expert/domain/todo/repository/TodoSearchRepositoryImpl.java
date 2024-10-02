package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

import static org.example.expert.domain.todo.entity.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoSearchRepositoryImpl implements TodoSearchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Todo findByIdFromQueryDsl(long todoId) {
        return queryFactory
                .select(todo)
                .from(todo)
                .where(
                        todo.id.eq(todoId)
                ).fetchOne();
    }

    private BooleanExpression todoIdEq(Long todoId) {
        return todoId != null ? todo.id.eq(todoId) : null;
    }
}
