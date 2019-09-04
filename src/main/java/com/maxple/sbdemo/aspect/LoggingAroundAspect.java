package com.maxple.sbdemo.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Aspect
public class LoggingAroundAspect {
    private Log log = LogFactory.getLog(getClass());

    @Around("execution(* com.maxple.sbdemo.service.CustomerService.*(..))")
    public Object log(@NotNull ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime start = LocalDateTime.now();

        try {
            return joinPoint.proceed();
        } finally {
            LocalDateTime stop = LocalDateTime.now();

            log.info("starting @ " + start);
            log.info("finishing @ " + stop + " with duration " + stop.minusNanos(start.getNano()).getNano());
        }
    }
}
