package com.example.aoptest.etc;

public class UserSession {
    private String userId;
    private String docBase;

    private VUser user;

    public UserSession() {}

    public UserSession(String userId, String docBase) {
        this.userId=userId;
        this.docBase=docBase;
    }

    public UserSession(String userId, String docBase, VUser user) {
        this.userId=userId;
        this.docBase=docBase;
        this.user=user;
    }
    public String getUserId() {
        return userId;
    }

    public String getDocBase() {
        return docBase;
    }
    public VUser getUser() {
        return user;
    }
}
