package com.hcmus.csc13009.smartenglish.utils;

import com.hcmus.csc13009.smartenglish.detection.tracking.MultiBoxTracker;
import com.hcmus.csc13009.smartenglish.detection.tracking.TrackerOption;

public class BoxTrackerUtils {
    public enum CameraMode {
        LEARN, TEST
    }
    public static CameraMode currentMode;

    public static void setMode(CameraMode mode) {
        if (currentMode == mode) // nothing happen
            return;
        currentMode = mode;
        switch (mode) {
            case LEARN:
                MultiBoxTracker.trackOption = TrackerOption.FULL;
                break;
            case TEST:
                MultiBoxTracker.trackOption = TrackerOption.BOX_ONLY;
                break;
        }
    }
}
