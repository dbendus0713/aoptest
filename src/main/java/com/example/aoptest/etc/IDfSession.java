package com.example.aoptest.etc;

public class IDfSession {
    private String userId;
    private String docBase;

    private boolean isTrans;
    private boolean isConnected;
    public IDfSession() {}
    public IDfSession(String userId, String docBase) {
        this.userId=userId;
        this.docBase=docBase;
    }

    public IDfSession(String userId, String docBase, boolean isTrans) {
        this.userId=userId;
        this.docBase=docBase;
        this.isTrans=isTrans;
    }

    public IDfSession(String userId, String docBase, boolean isTrans, boolean isConnected) {
        this.userId=userId;
        this.docBase=docBase;
        this.isTrans=isTrans;
        this.isConnected=isConnected;
    }

    public void abortTrans() {
        this.isTrans = false;
    }

    public void beginTrans() {
        this.isTrans = true;
    }

    public void commitTrans() {
        this.isTrans = false;
    }

    public boolean isTransactionActive() {
        return isTrans;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void disconnect() {
        this.isConnected = false;
    }
}
