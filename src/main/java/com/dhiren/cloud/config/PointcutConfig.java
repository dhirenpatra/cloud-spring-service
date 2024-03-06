package com.dhiren.cloud.config;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutConfig {

    @Pointcut("execution(* com.dhiren.cloud.*.*.*(..))")
    public void allPackage() {}

    @Pointcut("bean(*Service*)")
    public void allServiceBeans() {}

    @Pointcut("@annotation(com.dhiren.cloud.annotations.LogTimer)")
    public void allAnnotation() {}

}
