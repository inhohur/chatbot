package com.inho.chatbot.global.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {

	@Before("execution(* com.inho.chatbot..*Service.*(..))")
	public void beforeAdvice() {
		System.out.println("beforeAdvice() called");
	}
	
	@After("execution(* *..chatbot.*.*(..))")
	public void afterAdvice() {
		System.out.println("afterAdvice() called");
	}
	
	@AfterReturning("execution(* *..chatbot.*.*(..))")
	public void afterReturningAdvice() {
		System.out.println("afterReturningAdvice() called");
	}
	
	@AfterThrowing(value = "execution(* *..chatbot.*.*(..))", throwing="ex")
	public void afterThrowingAdvice(Throwable ex) {
		System.out.println("afterThrowingAdvice() called : " + ex);
	}
	
	@Around("execution(* *..*Service.*(..))")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		// before section
		long start = System.currentTimeMillis();
		System.out.println("aroundAdvice before ---");
		
		Object result = pjp.proceed();
		
		// after section
		long timePassed = System.currentTimeMillis() - start;
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		System.out.println("aroundAdvice after --- : " + timePassed + " millis : " + className + " -> " + methodName + "()");
		
		return result;
	}
	
}
