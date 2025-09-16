package com.example.app.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Pointcut for all controllers, services, repositories
    @Pointcut("execution(* com.example.app.controller..*(..)) || " +
              "execution(* com.example.app.service..*(..)) || " +
              "execution(* com.example.app.repository..*(..))")
    public void appLayer() {}

    // Around advice to log start, end, result, exceptions, and execution time
    @Around("appLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("[START] {} with args: {}", joinPoint.getSignature(), joinPoint.getArgs());
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            if (result instanceof Collection) {
                log.info("[END] {} returned {} records in {} ms", joinPoint.getSignature(), ((Collection<?>) result).size(), duration);
            } else {
                log.info("[END] {} returned: {} in {} ms", joinPoint.getSignature(), result, duration);
            }
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("[EXCEPTION] {} threw {}: {} after {} ms", joinPoint.getSignature(), ex.getClass().getSimpleName(), ex.getMessage(), duration, ex);
            throw ex;
        }
    }
}
