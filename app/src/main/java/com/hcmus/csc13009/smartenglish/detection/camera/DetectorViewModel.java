package com.hcmus.csc13009.smartenglish.detection.camera;

import android.app.Application;
import android.graphics.RectF;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.hcmus.csc13009.smartenglish.data.local.AppRepository;
import com.hcmus.csc13009.smartenglish.detection.tracking.MultiBoxTracker;

import java.util.List;

public class DetectorViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private int wrongCount = 0;

    public DetectorViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }

    public Pair<String, RectF> getObjectAtPosition(float x, float y, MultiBoxTracker tracker) {
        final List<Pair<String, RectF>> results = tracker.getTrackedObjects();
        Pair<String, RectF> object = null;
        for (Pair<String, RectF> result : results) {
            if (result.second.contains(x, y)) {
                if (selectionCondition(result, object, x, y)) {
                    object = result;
                }
            }
        }
        return object;
    }
    // select by center distance to touch point
    private boolean selectionCondition(Pair<String, RectF> newObject, Pair<String, RectF> oldObject, float x, float y) {
        if (oldObject == null)
            return true;
        double distanceNew = distance(newObject.second.centerX(), newObject.second.centerY(), x, y);
        double distanceOld = distance(oldObject.second.centerX(), oldObject.second.centerY(), x, y);
        return distanceNew < distanceOld;
    }

    private double distance(double x, double y, double u, double v) {
        return Math.sqrt(Math.pow(x - u, 2) + Math.pow(y - v, 2));
    }

    public void updateScore(@NonNull String label, int score) {
        repository.updateScore(label, score);
    }

    public boolean answerWrong() {
        wrongCount += 1;
        if (wrongCount >= 3) {
            wrongCount = 0;
            return true;
        }
        return false;
    }

}
