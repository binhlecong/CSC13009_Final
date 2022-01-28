package com.hcmus.csc13009.smartenglish.utils;

public class Question2 extends Question {
    public Question2(String target) {
        super(QUESTION_TYPE_2, target);
    }

    @Override
    public boolean validate(String answer) {
        return answer.equals(target);
    }
}
