package com.hcmus.csc13009.smartenglish.question;

import android.graphics.RectF;
import android.util.Pair;

import com.hcmus.csc13009.smartenglish.detection.camera.DetectorActivity;

import java.util.List;
import java.util.Random;

public class Question1 extends Question {
    public static final String QUESTION = "Tìm đồ vật bắt đầu bằng chữ cái:";

    public Question1() {
        super(QUESTION, null);
    }

    public Question1(String target) {
        super(QUESTION, target);
    }

    @Override
    public boolean validate(String answer) {
        if (answer == null || answer.isEmpty())
            return false;
        return answer.charAt(0) == getTarget().charAt(0);
    }

    @Override
    public void generateTarget(List<Pair<String, RectF>> trackedObject) {
        if (trackedObject == null || trackedObject.isEmpty()) {
            setTarget("l");
            return;
        }
        Random random = new Random();
        String title = trackedObject.get(random.nextInt(trackedObject.size())).first;
        setTarget(String.valueOf(title.charAt(0)));
    }
}
