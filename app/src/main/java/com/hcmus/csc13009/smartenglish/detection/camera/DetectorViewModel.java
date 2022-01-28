package com.hcmus.csc13009.smartenglish.detection.camera;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.hcmus.csc13009.smartenglish.detection.tflite.Detector;

import java.util.ArrayList;
import java.util.List;

public class DetectorViewModel extends AndroidViewModel {

    public DetectorViewModel(@NonNull Application application) {
        super(application);
    }

    public void handleOnTouch(MotionEvent event) {

    }
}
