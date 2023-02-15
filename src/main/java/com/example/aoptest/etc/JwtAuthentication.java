package com.example.aoptest.etc;

public class JwtAuthentication {
    public String loginId;

    public JwtAuthentication() {
    }

    public JwtAuthentication(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
