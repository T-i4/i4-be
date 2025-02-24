package com.business.i4_be.global.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AopLogging {
  private final ObjectMapper objectMapper;

  @Pointcut("execution(* com.business.i4_be.domain.*.controller..*(..))")
  private void aroundDomain() {}

  @Around("aroundDomain()")
  public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {

    String threadName = Thread.currentThread().getName();
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    log.info("현재 스레드: {}, 클래스 명: {} - 메서드 명: {}", threadName, className, methodName);

    long startTime = System.currentTimeMillis();

    try {
      Object result = joinPoint.proceed(); // 메서드 실행
      String jsonResult = objectMapper.writeValueAsString(result);
      log.info("반환 값 : {}", jsonResult);

      return result;
    } catch (Exception e) {
      log.error("예외 발생: {}", e.getMessage());
      throw e;
    } finally {
      long endTime = System.currentTimeMillis();
      log.info("실행 시간: {}", (endTime - startTime));
    }
  }
}
