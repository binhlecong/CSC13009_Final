package com.hcmus.csc13009.smartenglish.question;

import com.hcmus.csc13009.smartenglish.data.model.Quiz;
import com.hcmus.csc13009.smartenglish.detection.camera.DetectorActivity;

import java.util.List;

public class Question4 extends Question {

    public Question4(Quiz quiz) {
        super(quiz.getQuestion(), quiz.getAnswer());
    }

    @Override
    public boolean validate(String answer) {
        return target.contains(answer);
    }

    @Override
    public void generateTarget(List<String> trackedObject) {

    }

    @Override
    public void render(DetectorActivity activity) {
        activity.showTarget(null);
        activity.showRequest(getRequest());
        activity.showHelperImage(-1);
    }
}
