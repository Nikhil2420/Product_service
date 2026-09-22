package com.ProductService.backend.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class ServiceLoggingAspect {

    /*
     * = any return type | .. = package + subpackages | * = any method | (..) = any arguments
     */
    /*
            execution(
                * → return type
                com.ProductService.backend.service → package
                ..→ any subpackage OR directly in package
                * → method
                (..)→ arguments
            )
     */
    @Around("execution(* com.ProductService.backend.service..*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.nanoTime();

        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        log.info(
                "SERVICE HIT | Method: {} | Arguments: {}",
                methodName,
                Arrays.toString(arguments)
        );

        //real method call
        Object result = joinPoint.proceed();

        long executionTime =
                (System.nanoTime() - startTime) / 1_000_000;

        log.info(
                "SERVICE COMPLETED | Method: {} | Execution Time: {} ms",
                methodName,
                executionTime
        );

        return result;
    }
}
