package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.expert.aop.enums.MethodStatusEnum;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.log.entity.ManagerLog;
import org.example.expert.log.repository.LogRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class CommonLoggingAspect {

    private final LogRepository logRepository;
    private final HttpServletRequest request;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Around("execution(* org.example.expert.domain.manager.service.ManagerService.saveManager(..))")
    public Object logBeforeSaveManager(ProceedingJoinPoint joinPoint) throws Throwable {

        ManagerLog managerLog = new ManagerLog(LocalDateTime.now());

        long managerId = 0L;

        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            log.info("key : {}", entry.getKey());
            String[] values = entry.getValue();
            log.info("value : {}", Arrays.toString(values));

        }

//        Long todoId = Long.valueOf(request.getParameter("todoId"));

        logRepository.save(managerLog);

        log.info("saveMangerLog : {}", LocalDateTime.now());
        log.info("managerId : {}", managerId);
//        log.info("todoId : {}", todoId);

        try {
            joinPoint.proceed();
            managerLog.changeStatus(MethodStatusEnum.SUCCESS);

        } catch (Exception e) {
            managerLog.changeStatus(MethodStatusEnum.FAIL);
            log.info("Exception : {} ", e.getMessage());
        }
        return null;
    }
}
