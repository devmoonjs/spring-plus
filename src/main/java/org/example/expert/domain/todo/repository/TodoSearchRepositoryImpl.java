package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    public Page<TodoSearchResponse> searchTodos(Pageable pageable, TodoSearchRequest request) {
        List<TodoSearchResponse> results = queryFactory
                .select(
                        Projections.constructor(
                                TodoSearchResponse.class,
                                todo.title,
                                todo.managers.size().longValue(),
                                todo.comments.size().longValue()
                        )
                )
                .from(todo)
                .leftJoin(todo.managers)
                .leftJoin(todo.comments)
                .where(
                        titleContains(request.getTitle()),
                        nickNameContains(request.getNickName()),
                        byCreatedAt(LocalDate.parse(request.getCreatedAt(), DateTimeFormatter.ISO_DATE))
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = Optional.ofNullable(queryFactory
                .select(Wildcard.count)
                .from(todo)
                .where(
                        titleContains(request.getTitle()),
                        nickNameContains(request.getNickName()),
                        byCreatedAt(LocalDate.parse(request.getCreatedAt(), DateTimeFormatter.ISO_DATE))
                )
                .fetchOne()).orElse(0L);

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression titleContains(String searchTitle) {
        if (searchTitle == null) return null;
        return todo.title.contains(searchTitle);
    }

    private BooleanExpression nickNameContains(String nickName) {
        if (nickName == null) return null;
        return todo.user.nickname.contains(nickName);
    }

    private BooleanExpression byCreatedAt(LocalDate createdAt) {
        if (createdAt == null) return null;
        return todo.createdAt.after(createdAt.atStartOfDay());
    }
}
