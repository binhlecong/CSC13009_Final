package com.hcmus.csc13009.smartenglish.question;

import android.graphics.RectF;
import android.util.Pair;

import com.hcmus.csc13009.smartenglish.detection.camera.DetectorActivity;

import java.util.List;
import java.util.Random;

public class QuestionHandler {
    private Question question;
    private final Random generator = new Random();
    private final List<Pair<String, RectF>> trackedObject;
    // reference to activity
    private final DetectorActivity activity;


    public QuestionHandler(List<Pair<String, RectF>> trackedObject, DetectorActivity activity) {
        this.trackedObject = trackedObject;
        this.activity = activity;
    }
    // factory generate random question
    public void generateQuestion() {
        int questionType = generator.nextInt(2) + 1;
        switch (questionType) {
            case 1:
                setQuestion(new Question1());
                break;
            case 2:
                setQuestion(new Question2());
                break;
            default:
                setQuestion(new Question1());
                break;
        }
        this.question.generateTarget(trackedObject);
        this.question.render(activity);
    }

    public void setQuestion(Question q) {
        this.question = q;
    }

    public Question getQuestion() {
        return this.question;
    }

    public boolean validate(String answer) {
        return this.question.validate(answer);
    }
}
