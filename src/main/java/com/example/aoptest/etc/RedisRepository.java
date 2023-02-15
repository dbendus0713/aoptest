package com.example.aoptest.etc;

import org.springframework.stereotype.Component;

@Component
public class RedisRepository {
    public Object getObject(String loginId, Class<UserSession> userSessionClass) {
        return new UserSession(loginId, "aa", new VUser(loginId));
    }
}
