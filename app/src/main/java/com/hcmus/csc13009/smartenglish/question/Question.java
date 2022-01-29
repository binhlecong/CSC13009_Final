package com.hcmus.csc13009.smartenglish.question;

import android.graphics.RectF;
import android.util.Pair;

import com.hcmus.csc13009.smartenglish.detection.camera.DetectorActivity;

import java.util.List;

public abstract class Question {

    protected String request;
    protected String target;

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

    abstract public boolean validate(String answer);

    abstract public void generateTarget(List<Pair<String, RectF>> trackedObject);

    public void render(DetectorActivity activity) {
        activity.showTarget(getTarget());
        activity.showRequest(getRequest());
    }
}
