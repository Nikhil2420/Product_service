package com.ProductService.backend.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class RepositoryLoggingAspect {

    @Around("execution(* com.ProductService.backend.repository..*(..))")
    public Object repositoryLog(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.nanoTime();

        String methodName = joinPoint.getSignature().getName();

        log.info("REPOSITORY HIT | Method: {}", methodName);

        Object result = joinPoint.proceed();

        long executionTime =
                (System.nanoTime() - startTime) / 1_000_000;

        log.info(
                "REPOSITORY COMPLETED | Method: {} | Execution Time: {} ms",
                methodName,
                executionTime
        );

        return result;
    }
}
