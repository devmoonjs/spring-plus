package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearchRepository {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    @Query("SELECT t FROM Todo t WHERE t.createdAt BETWEEN :searchStart AND :searchEnd")
    Page<Todo> findAllTodo(Pageable pageable, LocalDateTime searchStart, LocalDateTime searchEnd);

    @Query("SELECT t FROM Todo t WHERE t.weather = :weather AND t.createdAt BETWEEN :searchStart AND :searchEnd")
    Page<Todo> findAllTodo(Pageable pageable, String weather, LocalDateTime searchStart, LocalDateTime searchEnd);

    @Query("""
            SELECT t FROM Todo t
            LEFT JOIN FETCH t.user u
            WHERE (:weather IS NULL OR t.weather = :weather)
            AND (:searchStartDate IS NULL OR t.modifiedAt >= :searchStartDate)
            AND (:searchEndDate IS NULL OR t.modifiedAt <= :searchEndDate)
            ORDER BY t.modifiedAt DESC
            """
    )
    Page<Todo> searchTodos(Pageable pageable, String weather, LocalDateTime searchStartDate, LocalDateTime searchEndDate);
}
