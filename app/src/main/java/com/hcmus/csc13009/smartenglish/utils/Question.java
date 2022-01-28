package com.hcmus.csc13009.smartenglish.utils;

public class Question {
    public static final String QUESTION_TYPE_1 = "Tìm đồ vật bắt đầu bằng kí tự sau:";
    public static final String QUESTION_TYPE_2 = "Hãy xác định vị trí của vật sau:";
    public static final String QUESTION_TYPE_3 = "Hãy cho biết số lượng của vật sau:";

    protected String request;
    protected String target;
    protected boolean result;

    public Question(String request, String target) {
        this.request = request;
        this.target = target;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean validate(String answer) {
        return true;
    }
}
