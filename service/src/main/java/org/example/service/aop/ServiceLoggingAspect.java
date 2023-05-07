package org.example.service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void isServiceLayer() {
    }

    @Before("isServiceLayer() " +
            "&& target(serviceClass)")
    public void addLoggingBeforeMethodCall(JoinPoint joinPoint,
                                           Object serviceClass) {
        log.info("Invoked {} method in class {} with args {}",
                joinPoint.getSignature().getName(), serviceClass, joinPoint.getArgs());
    }

    @AfterReturning(value = "isServiceLayer() " +
            "&& target(serviceClass)",
            returning = "result",
            argNames = "joinPoint,result,serviceClass")
    public void addLoggingAfterReturning(JoinPoint joinPoint,
                                         Object result,
                                         Object serviceClass) {
        log.info("Invoked {} method in class {}, result {}",
                joinPoint.getSignature().getName(), serviceClass, result);
    }

    @AfterThrowing(value = "isServiceLayer() && target(serviceClass)",
            throwing = "ex")
    public void addLoggingAfterThrowing(JoinPoint joinPoint,
                                        Throwable ex,
                                        Object serviceClass) {
        log.error("Invoked {} method in class {} exception {}: {}",
                joinPoint.getSignature().getName(), serviceClass, ex.getClass(), ex.getMessage());
    }
}
