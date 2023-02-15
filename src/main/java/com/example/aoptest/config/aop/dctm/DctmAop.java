package com.example.aoptest.config.aop.dctm;

import java.util.stream.IntStream;

import com.example.aoptest.config.aop.Documentum;
import com.example.aoptest.etc.IDfSession;
import com.example.aoptest.etc.UserSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class DctmAop {

  private final Logger logger = LoggerFactory.getLogger(DctmAop.class);

  private final Documentum documentum;
  private final ObjectMapper objectMapper;

  @Autowired
  public DctmAop(Documentum documentum, ObjectMapper objectMapper) {
    this.documentum = documentum;
    this.objectMapper = objectMapper;
  }

  @Pointcut("@annotation(com.example.aoptest.config.aop.dctm.DctmConnection)")
  public void targetMethod() {
  }

  @Around("targetMethod() && @annotation(dctmType)")
  public Object userLoginCheck(ProceedingJoinPoint joinPoint, DctmConnection dctmType) throws Throwable {
    Object resultObj = null;
    try {
      int userSessionIdx = getUserSessionIndex(joinPoint);
      UserSession userSession = objectMapper.convertValue(joinPoint.getArgs()[userSessionIdx], UserSession.class);
      int idfSessionIdx = getSessionIndex(joinPoint);

      boolean isTrans = TransType.START.equals(dctmType.transType());
      Object[] args = prepareArguments(joinPoint, dctmType.type(), userSession, idfSessionIdx);
      try {
        if (isTrans) ((IDfSession) args[idfSessionIdx]).beginTrans();
        resultObj = joinPoint.proceed(args);
        if (isTrans) ((IDfSession) args[idfSessionIdx]).commitTrans();
      } finally {
        releaseSession(args, userSession.getUser().getUserId(), dctmType.type(), idfSessionIdx);
      }
    } catch (Exception e) {
      logger.error("DCTM AOP ERROR", e);
      throw e;
    }
    return resultObj;
  }

  private int getUserSessionIndex(ProceedingJoinPoint joinPoint) {
    return IntStream.range(0, joinPoint.getArgs().length)
        .filter(idx -> joinPoint.getArgs()[idx] instanceof UserSession)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("One UserSession parameter is required"));
  }

  private int getSessionIndex(ProceedingJoinPoint joinPoint) {
    int foundIdx = IntStream.range(0, joinPoint.getArgs().length)
                .filter(idx -> joinPoint.getArgs()[idx] instanceof IDfSession || joinPoint.getArgs()[idx] == null)
                .findFirst()
                .orElse(-1);
    if (foundIdx == -1) {
      return IntStream.range(0, joinPoint.getArgs().length)
          .filter(idx -> joinPoint.getArgs()[idx] == null)
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("At least one type IDfSession parameter is required"));
    }
    return foundIdx;
  }


  private void releaseSession(Object[] args, String loginId, DctmAopType type, int idfSessionIdx) {
    if (((IDfSession) args[idfSessionIdx]) != null) {
      if (((IDfSession) args[idfSessionIdx]).isTransactionActive()) {
        ((IDfSession) args[idfSessionIdx]).abortTrans();
      }
      switch (type) {
        case CREATE_USER:
        case EXIST_USER:
          if (((IDfSession) args[idfSessionIdx]).isConnected()) {
            documentum.release(loginId, ((IDfSession) args[idfSessionIdx]));
          }
          break;
        case CREATE_ADMIN:
        case EXIST_ADMIN:
          if (((IDfSession) args[idfSessionIdx]).isConnected()) {
            ((IDfSession) args[idfSessionIdx]).disconnect();
          }
          break;
      }
    }
  }

  private Object[] prepareArguments(ProceedingJoinPoint joinPoint, DctmAopType type, UserSession userSession, int idfSessionIdx)
      throws Throwable { 
    switch (type) {
      case CREATE_USER:
      {
        joinPoint.getArgs()[idfSessionIdx] = documentum.getSession(userSession);
        break;
      }
      case CREATE_ADMIN:
      {
        joinPoint.getArgs()[idfSessionIdx] = documentum.getAdminSession();
        break;
      }
      case EXIST_ADMIN:
      case EXIST_USER: 
        if (joinPoint.getArgs()[idfSessionIdx] == null) {
          throw new IllegalArgumentException("IdfSession is null but DctmAopType is EXIST_SOMTHING");
        } else if (!((IDfSession) joinPoint.getArgs()[idfSessionIdx]).isConnected()) {
          throw new IllegalArgumentException("IdfSession is exist but is not connected");
        }
    }
    return joinPoint.getArgs();
  }
}