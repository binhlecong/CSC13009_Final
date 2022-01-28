package com.hcmus.csc13009.smartenglish.question;

import android.graphics.RectF;
import android.util.Pair;

import java.util.List;

public class Question3 extends Question {
    public static final String QUESTION = "Hãy cho biết số lượng của vật sau:";

    public Question3(String target) {
        super(QUESTION, target);
    }

    @Override
    public boolean validate(String answer) {
        // TODO: go get number of object name target and compare with answer
        return true;
    }

    @Override
    public void generateTarget(List<Pair<String, RectF>> trackedObject) {

    }
}
