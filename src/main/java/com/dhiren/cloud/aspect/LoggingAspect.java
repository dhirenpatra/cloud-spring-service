package com.dhiren.cloud.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Before("execution(* com.dhiren.cloud.*.*.*(..))")
    public void logMethodCallsBefore(JoinPoint jp) {
        logger.info("Method intercepted for {} started execution", jp.getSignature().getName());
    }

    @After("execution(* com.dhiren.cloud.*.*.*(..))")
    public void logMethodCallsAfter(JoinPoint jp) {
        logger.info("Method intercepted for {} is executed", jp.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* com.dhiren.cloud.*.*.*(..))",
    throwing = "exception")
    public void logMethodCallsAfterThrowing(JoinPoint jp, Exception exception) {
        logger.info("Method intercepted for {} is executed with exception {} ", jp.getSignature().getName(),
                exception.getCause().getMessage());
    }

}
