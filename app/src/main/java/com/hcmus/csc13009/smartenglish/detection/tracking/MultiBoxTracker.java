/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.hcmus.csc13009.smartenglish.detection.tracking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Pair;
import android.util.TypedValue;

import com.hcmus.csc13009.smartenglish.detection.env.BorderedText;
import com.hcmus.csc13009.smartenglish.detection.env.ImageUtils;
import com.hcmus.csc13009.smartenglish.detection.env.Logger;
import com.hcmus.csc13009.smartenglish.detection.tflite.Detector.Recognition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

/** A tracker that handles non-max suppression and matches existing objects to new detections. */
public class MultiBoxTracker {
    // setting for test/learn mode
    public static TrackerOption trackOption = TrackerOption.FULL;
    private static final int MAX_STORAGE_OBJECT = 8; // maximum storage object for generate question

    private static final float TEXT_SIZE_DIP = 18;
    private static final float MIN_SIZE = 16.0f;
    private static final int[] COLORS = {
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA,
            Color.WHITE,
            Color.parseColor("#55FF55"),
            Color.parseColor("#FFA500"),
            Color.parseColor("#FF8888"),
            Color.parseColor("#AAAAFF"),
            Color.parseColor("#FFFFAA"),
            Color.parseColor("#55AAAA"),
            Color.parseColor("#AA33AA"),
            Color.parseColor("#0D0068")
    };
    final List<Pair<Float, RectF>> screenRects = new LinkedList<>();
    private final Logger logger = new Logger();
    private final Queue<Integer> availableColors = new LinkedList<>();
    private final List<TrackedRecognition> trackedObjects = new LinkedList<>();
    private final Paint boxPaint = new Paint();
    private final float textSizePx;
    private final BorderedText borderedText;
    private Matrix frameToCanvasMatrix;
    private int frameWidth;
    private int frameHeight;
    private int sensorOrientation;
    // tracked list label and location of object
    private final List<Pair<String, RectF>> objectOnScreen = new LinkedList<>();
    // store top object
    private final HashMap<String, Float> storageObject = new HashMap<>();

    public MultiBoxTracker(final Context context) {
        for (final int color : COLORS) {
            availableColors.add(color);
        }

        boxPaint.setColor(Color.RED);
        boxPaint.setStyle(Style.STROKE);
        boxPaint.setStrokeWidth(10.0f);
        boxPaint.setStrokeCap(Cap.ROUND);
        boxPaint.setStrokeJoin(Join.ROUND);
        boxPaint.setStrokeMiter(100);

        textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, context.getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
    }
    // get all tracked object' label and position
    public List<Pair<String, RectF>> getTrackedObjects() {
        return objectOnScreen;
    }
    // get list of storage object
    public List<String> getStorageObject() {
        return new ArrayList<>(storageObject.keySet());
    }


    public synchronized void setFrameConfiguration(
            final int width, final int height, final int sensorOrientation) {
        frameWidth = width;
        frameHeight = height;
        this.sensorOrientation = sensorOrientation;
    }

    public synchronized void drawDebug(final Canvas canvas) {
        final Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60.0f);

        final Paint boxPaint = new Paint();
        boxPaint.setColor(Color.RED);
        boxPaint.setAlpha(200);
        boxPaint.setStyle(Style.STROKE);

        for (final Pair<Float, RectF> detection : screenRects) {
            final RectF rect = detection.second;
            canvas.drawRect(rect, boxPaint);
            canvas.drawText("" + detection.first, rect.left, rect.top, textPaint);
            borderedText.drawText(canvas, rect.centerX(), rect.centerY(), "" + detection.first);
        }
    }

    public synchronized void trackResults(final List<Recognition> results, final long timestamp) {
        logger.i("Processing %d results from %d", results.size(), timestamp);
        processResults(results);
    }

    private Matrix getFrameToCanvasMatrix() {
        return frameToCanvasMatrix;
    }

    public synchronized void draw(final Canvas canvas) {
        final boolean rotated = sensorOrientation % 180 == 90;
        final float multiplier =
                Math.min(
                        canvas.getHeight() / (float) (rotated ? frameWidth : frameHeight),
                        canvas.getWidth() / (float) (rotated ? frameHeight : frameWidth));
        frameToCanvasMatrix =
                ImageUtils.getTransformationMatrix(
                        frameWidth,
                        frameHeight,
                        (int) (multiplier * (rotated ? frameHeight : frameWidth)),
                        (int) (multiplier * (rotated ? frameWidth : frameHeight)),
                        sensorOrientation,
                        false);
        // TODO: label category here
        objectOnScreen.clear();
        for (final TrackedRecognition recognition : trackedObjects) {
            final RectF trackedPos = new RectF(recognition.location);

            getFrameToCanvasMatrix().mapRect(trackedPos);
            boxPaint.setColor(recognition.color);
            // put into tracked list on screen
            if (recognition.title.equals("tv"))
                recognition.title = "television";
            objectOnScreen.add(new Pair<>(recognition.title, trackedPos));
            if (trackOption == TrackerOption.NOTHING)
                continue;

            float cornerSize = Math.min(trackedPos.width(), trackedPos.height()) / 8.0f;
            if (trackOption == TrackerOption.BOX_ONLY || trackOption == TrackerOption.FULL) {
                canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint);
            }

            if (trackOption == TrackerOption.FULL || trackOption == TrackerOption.LABEL_ONLY) {
                String labelString =
                        !TextUtils.isEmpty(recognition.title)
                                ? String.format(Locale.getDefault(), "%s %.2f", recognition.title, (100 * recognition.detectionConfidence))
                                : String.format(Locale.getDefault(),"%.2f", (100 * recognition.detectionConfidence));
                //            borderedText.drawText(canvas, trackedPos.left + cornerSize, trackedPos.top,
                // labelString);
                if (trackOption == TrackerOption.FULL) {
                    borderedText.drawText(
                            canvas, trackedPos.left + cornerSize, trackedPos.top, labelString + "%", boxPaint);
                } else {
                    float midPosLeft = (trackedPos.left + trackedPos.right) / 2;
                    float midPosTop = (trackedPos.top + trackedPos.bottom) / 2;
                    borderedText.drawText(
                            canvas, midPosLeft, midPosTop, recognition.title, boxPaint);
                }
            }
        }
    }

    private void processResults(final List<Recognition> results) {
        final List<Pair<Float, Recognition>> rectsToTrack = new LinkedList<>();

        screenRects.clear();
        final Matrix rgbFrameToScreen = new Matrix(getFrameToCanvasMatrix());

        for (final Recognition result : results) {
            if (result.getLocation() == null) {
                continue;
            }
            final RectF detectionFrameRect = new RectF(result.getLocation());

            final RectF detectionScreenRect = new RectF();
            rgbFrameToScreen.mapRect(detectionScreenRect, detectionFrameRect);

            logger.v(
                    "Result! Frame: " + result.getLocation() + " mapped to screen:" + detectionScreenRect);

            screenRects.add(new Pair<>(result.getConfidence(), detectionScreenRect));

            if (detectionFrameRect.width() < MIN_SIZE || detectionFrameRect.height() < MIN_SIZE) {
                logger.w("Degenerate rectangle! " + detectionFrameRect);
                continue;
            }

            rectsToTrack.add(new Pair<>(result.getConfidence(), result));
            updateStorageObject(result.getTitle(), result.getConfidence());
        }

        trackedObjects.clear();
        if (rectsToTrack.isEmpty()) {
            logger.v("Nothing to track, aborting.");
            return;
        }

        for (final Pair<Float, Recognition> potential : rectsToTrack) {
            final TrackedRecognition trackedRecognition = new TrackedRecognition();
            trackedRecognition.detectionConfidence = potential.first;
            trackedRecognition.location = new RectF(potential.second.getLocation());
            trackedRecognition.title = potential.second.getTitle();
            trackedRecognition.color = COLORS[trackedObjects.size()];
            trackedObjects.add(trackedRecognition);

            if (trackedObjects.size() >= COLORS.length) {
                break;
            }
        }
    }

    public static class TrackedRecognition {
        public RectF location;
        float detectionConfidence;
        int color;
        public String title;
    }

    // helper for learn english feature
    private void updateStorageObject(String label, float confidence) {
        if (storageObject.containsKey(label)) {
            if (confidence <= 0) {
                storageObject.remove(label);
            } else if (storageObject.get(label) < confidence) {
                storageObject.put(label, confidence);
            }
            return;
        }
        if (storageObject.size() < MAX_STORAGE_OBJECT) { // store if still has space
            storageObject.put(label, confidence);
        } else { // replace minimum confidence object
            Map.Entry<String, Float> lowestObject = Collections.min(storageObject.entrySet(),
                    (t1, t2) -> t1.getValue().compareTo(t2.getValue()));
            if (lowestObject.getValue() < confidence) {
                storageObject.remove(lowestObject.getKey());
                storageObject.put(label, confidence);
            }
        }
    }
}
