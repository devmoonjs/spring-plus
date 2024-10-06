package org.example.expert.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.aop.enums.MethodStatusEnum;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class ManagerLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    private Long todoId;

    private Long managerId;

    @Enumerated(EnumType.STRING)
    private MethodStatusEnum status;

    public ManagerLog(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void changeStatus(MethodStatusEnum status) {
        this.status = status;
    }
}
