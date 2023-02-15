package com.example.aoptest.config.aop.usersession;

import java.util.Arrays;


import com.example.aoptest.etc.JwtAuthentication;
import com.example.aoptest.etc.RedisRepository;
import com.example.aoptest.etc.UserSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(value = 2)
public class IUserAspect {

  @Autowired
  private RedisRepository redisRepository;

  @Around("execution(* *(.., @IUserSession (*), ..))")
  public Object convertUser(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = Arrays.stream(joinPoint.getArgs()).map(data -> { 
      if (data instanceof UserSession) {
        for (Object obj : joinPoint.getArgs()) {
          if (obj instanceof JwtAuthentication) {
            String loginId = ((JwtAuthentication) obj).loginId;
            data = (UserSession) redisRepository.getObject(loginId, UserSession.class);
            break;
          }
        }
      }
      return data; 
    }).toArray();
    return joinPoint.proceed(args);
  }
}
