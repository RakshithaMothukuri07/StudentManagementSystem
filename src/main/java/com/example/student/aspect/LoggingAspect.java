
package com.example.student.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.student.service.StudentService.*(..))")
    public void beforeServiceMethod() {
        System.out.println("Before: Student service method is starting");
    }

    @After("execution(* com.example.student.service.StudentService.*(..))")
    public void afterServiceMethod() {
        System.out.println("After: Student service method has finished");
    }

    @Around("execution(* com.example.student.service.StudentService.*(..))")
    public Object aroundServiceMethod(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long startTime = System.currentTimeMillis();

        System.out.println("Around: Starting "
                + joinPoint.getSignature().getName());

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            System.out.println("Around: Finished "
                    + joinPoint.getSignature().getName()
                    + " in " + duration + " ms");
        }
    }
}
