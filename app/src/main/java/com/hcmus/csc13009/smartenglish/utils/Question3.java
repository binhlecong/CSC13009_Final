package com.hcmus.csc13009.smartenglish.utils;

public class Question3 extends Question {
    public Question3(String target) {
        super(QUESTION_TYPE_3, target);
    }

    @Override
    public boolean validate(String answer) {
        return answer.charAt(0) == target.charAt(0);
    }
}
