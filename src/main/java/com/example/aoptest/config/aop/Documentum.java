package com.example.aoptest.config.aop;


import com.example.aoptest.etc.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Documentum {
    private Logger logger = LoggerFactory.getLogger(Documentum.class);
    //  private final HashMap<String, IDfSessionManager> sessionMap = new HashMap<>();
    LoadingCache<String, Object> sessionCache = CacheBuilder.newBuilder()
            .maximumSize(30000)
//      .expireAfterWrite(Commons.DCTM_SESSION_DURATION_MIN, TimeUnit.MINUTES)
            .expireAfterAccess(86400, TimeUnit.MINUTES)
            // 필요시 개발해야함
//      .removalListener(MY_LISTENER)
            .build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) throws Exception {
                    return null;
                }
            });

    private Object createSessionManager(String userName, String userPassword, String docbrokerHost,
                                                   String docbrokerPort) throws Exception {
        return new Object();
    }

    public void makeSessionManager(UserSession userSession) throws Exception {

    }

    public IDfSession getSession(UserSession userSession) throws Exception {
        return this.getSession(userSession.getUserId(), userSession.getDocBase());
    }

    public IDfSession getSession(String userId, String docBase) throws Exception {
        return new IDfSession(userId, docBase);
    }

    public void release(String dUserId, IDfSession session) {
    }

    public IDfSession getAdminSession() {
        return new IDfSession();
    }
}