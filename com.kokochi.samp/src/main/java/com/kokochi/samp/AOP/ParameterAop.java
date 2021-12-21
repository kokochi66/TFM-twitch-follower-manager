package com.kokochi.samp.AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
public class ParameterAop {

    @Pointcut("execution(* com.kokochi.samp.controller..*.*(..))")
    private void cut() {}

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println("Aspect :: " + method.getName() + "메서드 실행");

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            System.out.println("Aspect :: " + "type :: " + arg.getClass().getSimpleName());
            System.out.println("Aspect :: " + "value :: " + arg);
        }
    }

    @AfterReturning(value = "cut()", returning = "obj")
    public void afterReturn(JoinPoint joinPoint, Object obj) {
        System.out.println("Aspect :: " + "return obj");
        System.out.println("Aspect :: " + obj);
    }


}
