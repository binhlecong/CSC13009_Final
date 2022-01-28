package com.hcmus.csc13009.smartenglish.utils;

public class Question1 extends Question {
    public Question1(String target) {
        super(QUESTION_TYPE_1, target);
    }

    @Override
    public boolean validate(String answer) {
        return answer.charAt(0) == target.charAt(0);
    }
}
