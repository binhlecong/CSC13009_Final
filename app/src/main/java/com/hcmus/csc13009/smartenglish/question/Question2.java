package com.hcmus.csc13009.smartenglish.question;

import android.graphics.RectF;
import android.util.Pair;

import java.util.List;
import java.util.Random;

public class Question2 extends Question {
    public static final String QUESTION = "Hãy xác định vị trí của vật sau:";

    public Question2() {
        super(QUESTION, null);
    }

    public Question2(String target) {
        super(QUESTION, target);
    }

    @Override
    public boolean validate(String answer) {
        if (answer == null || answer.isEmpty())
            return false;
        return answer.equalsIgnoreCase(getTarget());
    }

    // generate a label
    @Override
    public void generateTarget(List<Pair<String, RectF>> trackedObject) {
        if (trackedObject == null || trackedObject.isEmpty()) {
            setTarget("laptop");
            return;
        }
        Random random = new Random();
        setTarget(trackedObject.get(random.nextInt(trackedObject.size())).first);
    }
}
