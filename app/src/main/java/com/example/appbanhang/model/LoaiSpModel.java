package com.example.appbanhang.model;

import java.util.List;

public class LoaiSpModel {
    boolean success;
    String massage;
    List<LoaiSP> result;

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

    public List<LoaiSP> getResult() {
        return result;
    }

    public void setResult(List<LoaiSP> result) {
        this.result = result;
    }
}
