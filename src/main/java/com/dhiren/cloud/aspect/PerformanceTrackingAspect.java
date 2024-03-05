package com.dhiren.cloud.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class PerformanceTrackingAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Around("execution(* com.dhiren.cloud.*.*.*(..))")
    public Object timeTaken(ProceedingJoinPoint joinPoint) throws Throwable {
        var startTime = System.currentTimeMillis();
        var result = joinPoint.proceed();
        logger.info("Method intercepted for {} started execution", joinPoint.getSignature().getName());
        var stopTime = System.currentTimeMillis();
        var executionDuration = stopTime - startTime;
        logger.info("Method took {}ms time", executionDuration);
        return result;
    }

}
