package com.lazyafei.springpractice.conf;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut that matches all repositories, services and resource endpoints.
     * TODO 待改造完成后，换回注释的注解
     *
     * "
     *             + "|| within(com.easemob.oa.rest.exception..*)"
     *             + "|| within(com.easemob.oa.services.impl..*)"
     *             + "|| within(com.easemob.oa.services.schedule..*)"
     *             + "|| within(com.easemob.oa.services.im..*)"
     *             + "|| within(com.easemob.oa.persistence.jpa..*)"
     *             + "|| within(com.easemob.oa.services.event.listener..*)
     */
    @Pointcut("within(com.lazyafei.springpractice.controller..*)")
//    @Pointcut("execution(public * com.easemob.oa.rest.controller.v1.DiskInfoController.*(..))"
//            +"|| execution(public * com.easemob.oa.rest.controller.v1.CatalogController.*(..))"
//            +"|| execution(public * com.easemob.oa.services.impl.CatalogServiceImpl.*(..))"
//            +"|| execution(public * com.easemob.oa.services.impl.DiskInfoServiceImpl.*(..))"
//            +"|| within(com.easemob.oa.services.event..*)")
    public void loggingPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the
        // advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     */
    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'",
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);

    }

    /**
     * Advice that logs when a method is entered and exited.
     */
    @Around("loggingPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //if (log.isDebugEnabled()) {
            log.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        //}
        try {
            Object result = joinPoint.proceed();
          //  if (log.isDebugEnabled()) {
                log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
         //   }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
