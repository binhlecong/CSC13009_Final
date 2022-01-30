/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hcmus.csc13009.smartenglish.detection.camera;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.hcmus.csc13009.smartenglish.detection.R;
import com.hcmus.csc13009.smartenglish.detection.customview.OverlayView;
import com.hcmus.csc13009.smartenglish.detection.env.BorderedText;
import com.hcmus.csc13009.smartenglish.detection.env.ImageUtils;
import com.hcmus.csc13009.smartenglish.detection.env.Logger;
import com.hcmus.csc13009.smartenglish.detection.tflite.Detector;
import com.hcmus.csc13009.smartenglish.detection.tflite.TFLiteObjectDetectionAPIModel;
import com.hcmus.csc13009.smartenglish.detection.tracking.MultiBoxTracker;
import com.hcmus.csc13009.smartenglish.question.QuestionHandler;
import com.hcmus.csc13009.smartenglish.utils.TextToSpeechUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener {
    // Minimum detection confidence to track a detection.
    public static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.6f;
    private static final Logger LOGGER = new Logger();
    // Configuration values for the prepackaged SSD model.
    private static final int TF_OD_API_INPUT_SIZE = 300;
    private static final boolean TF_OD_API_IS_QUANTIZED = true;
    private static final String TF_OD_API_MODEL_FILE = "detect.tflite";
    private static final String TF_OD_API_LABELS_FILE = "labelmap.txt";
    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    private static final boolean MAINTAIN_ASPECT = false;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;

    private Detector detector;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingDetection = false;

    private long timestamp = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;

    private QuestionHandler questionHandler;
    private boolean isRunningQuestion = false;

    private DetectorViewModel viewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DetectorViewModel.class);
        String mode = getIntent().getStringExtra("mode");
        // TODO: fix bug show score
        if (mode != null && mode.equals("exam")) {
            setTestModeOn();
        }
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP,
                        getResources().getDisplayMetrics());
        BorderedText borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        tracker = new MultiBoxTracker(this);

        int cropSize = TF_OD_API_INPUT_SIZE;

        try {
            detector =
                    TFLiteObjectDetectionAPIModel.create(
                            this,
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_INPUT_SIZE,
                            TF_OD_API_IS_QUANTIZED);
            cropSize = TF_OD_API_INPUT_SIZE;
        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.e(e, "Exception initializing Detector!");
            Toast toast = Toast.makeText(getApplicationContext(), "Detector could not be " +
                    "initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        int sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        cropSize, cropSize,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        trackingOverlay = findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                canvas -> {
                    tracker.draw(canvas);
                    if (isDebug()) {
                        tracker.drawDebug(canvas);
                    }
                });
        trackingOverlay.setOnTouchListener((view, motionEvent) -> onOverlayViewTouchEvent(motionEvent));

        tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);



    }

    @Override
    protected void processImage() {
        ++timestamp;
        final long currTimestamp = timestamp;
        trackingOverlay.postInvalidate();

        // No mutex needed as this method is not reentrant.
        if (computingDetection) {
            readyForNextImage();
            return;
        }
        computingDetection = true;
        LOGGER.i("Preparing image " + currTimestamp + " for detection in bg thread.");

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

        readyForNextImage();

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }
        // run detector
        runInBackground(() -> {
            LOGGER.i("Running detection on image " + currTimestamp);
            final long startTime = SystemClock.uptimeMillis();
            final List<Detector.Recognition> results =
                    detector.recognizeImage(croppedBitmap);
            lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

            cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
            final Canvas canvas1 = new Canvas(cropCopyBitmap);
            final Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(2.0f);

            float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
            if (MODE == DetectorMode.TF_OD_API) {
                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
            }

            final List<Detector.Recognition> mappedRecognitions =
                    new ArrayList<>();

            for (final Detector.Recognition result : results) {
                final RectF location = result.getLocation();
                if (location != null && result.getConfidence() >= minimumConfidence) {
                    cropToFrameTransform.mapRect(location);
                    result.setLocation(location);
                    mappedRecognitions.add(result);
                }
            }

            tracker.trackResults(mappedRecognitions, currTimestamp);
            trackingOverlay.postInvalidate();

            computingDetection = false;

            runOnUiThread(() -> {
                if (activityMode == TEST_MODE) {
                    if (isRunningQuestion) return;
                    // Choose question type in test mode
                    if (questionHandler == null)
                        questionHandler = new QuestionHandler(tracker, this);

                    questionHandler.generateQuestion();
                    isRunningQuestion = true;
                } else {
                    isRunningQuestion = false;
                    showRequest("Hãy tìm đồ vật và chạm vào nó");
                }
            });
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tfe_od_camera_connection_fragment_tracking;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        runInBackground(() -> {
            try {
                detector.setUseNNAPI(isChecked);
            } catch (UnsupportedOperationException e) {
                LOGGER.e(e, "Failed to set \"Use NNAPI\".");
                runOnUiThread(
                        () -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        runInBackground(() -> detector.setNumThreads(numThreads));
    }

    public boolean onOverlayViewTouchEvent(MotionEvent event) {
        int maskedAction = event.getActionMasked();
        if (maskedAction != MotionEvent.ACTION_DOWN)
            return false;
        showRespond("");
        float x = event.getX();
        float y = event.getY();
        Pair<String, RectF> result = viewModel.getObjectAtPosition(x, y, tracker);
        if (result != null) {
            if (activityMode == LEARN_MODE) {
                showTarget(result.first);
                TextToSpeechUtils.speak(getApplicationContext(), result.first);
            } else {
                boolean isCorrect = questionHandler.validate(result.first);
                TextToSpeechUtils.speak(getApplicationContext(), isCorrect ? "Correct" : "Wrong");
                if (isCorrect) {
                    isRunningQuestion = false;
                    viewModel.updateScore(result.first, 1);
                    showAnimation(true, x, y);
                    setScore(getScore() + 1);
                } else {
                    if (viewModel.answerWrong()) {
                        viewModel.updateScore(questionHandler.getQuestion().getTarget(), -1);
                        isRunningQuestion = false;
                    }
                    showAnimation(false, x, y);
                    showRespond("Sorry! That is a " + result.first);
                    setScore(getScore() - 1);
                }
            }
        }
        return true;
    }

    private void showAnimation(boolean isCorrect, float x, float y) {
        ImageView imageView = findViewById(isCorrect ? R.id.correct_answer : R.id.wrong_answer);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setVisibility(View.VISIBLE);
        Animation fadeAnim = new AlphaAnimation(1f, 0f);
        fadeAnim.setDuration(1000);
        fadeAnim.setFillAfter(true);

        if (isCorrect) {
            imageView.animate().translationY(y - 50).setDuration(1000);
        } else {
            imageView.animate().translationY(y + 50).setDuration(1000);
        }
        imageView.startAnimation(fadeAnim);
    }

    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum DetectorMode {
        TF_OD_API
    }
}
