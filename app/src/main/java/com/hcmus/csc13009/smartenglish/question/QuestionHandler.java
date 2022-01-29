package com.hcmus.csc13009.smartenglish.question;

import com.hcmus.csc13009.smartenglish.detection.camera.DetectorActivity;
import com.hcmus.csc13009.smartenglish.detection.tracking.MultiBoxTracker;

import java.util.Random;

public class QuestionHandler {
    private Question question;
    private final Random generator = new Random();
    // reference to object detection tracker
    private final MultiBoxTracker tracker;
    // reference to activity
    private final DetectorActivity activity;


    public QuestionHandler(MultiBoxTracker tracker, DetectorActivity activity) {
        this.tracker = tracker;
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
//        Log.i("@@@ tracked: ", tracker.getStorageObject().toString());
        this.question.generateTarget(tracker.getStorageObject());
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
