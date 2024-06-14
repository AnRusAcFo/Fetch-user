package com.example.appbanhang.model;

import java.util.List;

public class MeetingModel {
    boolean success;
    String massage;
    List<Meetingg> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public List<Meetingg> getResult() {
        return result;
    }

    public void setResult(List<Meetingg> result) {
        this.result = result;
    }
}
