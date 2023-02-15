package com.example.aoptest.config.aop.dctm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UserSession must be provided
 * IDfSession must be provided at least 1. 
 * if several IDfSession params are provided,  preceding one will be used
 * if IdfSession is not provided, type of null will be treated as IDfSession param.
 * if null is treated as IDfSession, DctmAopType must start with CREATE_[]
 * _
 *   example: function aa(String, IdfSession, UserSession)
 *            call aa("String Value!", null, userSession);   
 *            type must be DctmAopType.CREATE_USER or DctmAopType.CREATE_ADMIN
 * _
 *   example: function aa(String, IdfSession1, IdfSession2, UserSession)
 *            only IdfSession1 will be treated by @DctmConnection
 * @author  dy; 
 * @since 2023
 */
@Target({ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface DctmConnection {

  DctmAopType type() default DctmAopType.CREATE_USER;
  TransType transType();
}
