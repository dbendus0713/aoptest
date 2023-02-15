package com.example.aoptest.service;

import com.example.aoptest.config.aop.dctm.DctmAopType;
import com.example.aoptest.config.aop.dctm.DctmConnection;
import com.example.aoptest.config.aop.dctm.TransType;
import com.example.aoptest.etc.IDfSession;
import com.example.aoptest.etc.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestService {


    private final Logger logger = LoggerFactory.getLogger(TestService.class);

    @DctmConnection(type = DctmAopType.CREATE_USER, transType= TransType.START)
    public void doSomething(UserSession userSession, IDfSession iDfSession, String tmp) {
        logger.debug("userSession----", userSession);
        logger.debug("iDfSession----", iDfSession);
        logger.debug("tmp----", tmp);
    }
}
