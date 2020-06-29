package com.cegep.saporiitaliano.model;

public class PushNotification {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public PushNotification(String status) {
        this.status = status;
    }
}
