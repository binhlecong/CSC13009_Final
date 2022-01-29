package com.hcmus.csc13009.smartenglish.question;

import android.widget.ImageView;

import com.hcmus.csc13009.smartenglish.detection.R;
import com.hcmus.csc13009.smartenglish.detection.camera.DetectorActivity;
import com.hcmus.csc13009.smartenglish.utils.TextToSpeechUtils;

import java.util.List;
import java.util.Random;

public class Question3 extends Question {
    public static final String QUESTION = "Hãy lắng nghe và tìm đồ vật";

    public Question3() {
        super(QUESTION, "");
    }

    @Override
    public boolean validate(String answer) {
        return answer.equals(getTarget());
    }

    @Override
    public void generateTarget(List<String> trackedObject) {
        if (trackedObject == null || trackedObject.isEmpty()) {
            setTarget("laptop");
            return;
        }
        Random random = new Random();
        setTarget(trackedObject.get(random.nextInt(trackedObject.size())));
    }

    @Override
    public void render(DetectorActivity activity) {
        activity.showTarget(null);
        activity.showRequest(getRequest());

        ImageView imageView = activity.getHelperImageView();
        activity.showHelperImage(R.drawable.ic_speaker);
        imageView.setOnClickListener(view -> TextToSpeechUtils.speak(activity.getApplicationContext(), getTarget()));
        TextToSpeechUtils.speak(activity.getApplicationContext(), getTarget());
    }
}
