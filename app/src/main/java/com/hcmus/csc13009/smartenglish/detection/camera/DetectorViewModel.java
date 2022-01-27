package com.hcmus.csc13009.smartenglish.detection.camera;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.hcmus.csc13009.smartenglish.detection.tflite.Detector;

import java.util.ArrayList;
import java.util.List;

public class DetectorViewModel extends AndroidViewModel {

    public DetectorViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Detector.Recognition> trackObject(Detector detector, Bitmap croppedBitMap, Canvas canvas) {
        final List<Detector.Recognition> results =
                detector.recognizeImage(croppedBitMap);
        final List<Detector.Recognition> mappedRecognitions =
                new ArrayList<>();

        for (final Detector.Recognition result : results) {
            final RectF location = result.getLocation();
            if (location != null && result.getConfidence() >= DetectorActivity.MINIMUM_CONFIDENCE_TF_OD_API) {
                result.setLocation(location);
                mappedRecognitions.add(result);
            }
        }
        return mappedRecognitions;
    }
}
