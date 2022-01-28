package com.hcmus.csc13009.smartenglish.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class TextToSpeechUtils {
    static TextToSpeech speak = null;

    static public void init(Context context) {
        speak = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                speak.setLanguage(Locale.US);
            } else {
                Toast.makeText(context,
                        "Error initializing TTS",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    static public int setLanguage(Context context, Locale language) {
        if (speak == null)
            init(context);
        Toast.makeText(context,
                "Language is set to " + language.getLanguage(),
                Toast.LENGTH_SHORT).show();
        return speak.setLanguage(language);
    }

    static public void speak(Context context, String text) {
        if (speak == null) {
            speak = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    speak.setLanguage(Locale.US);
                    int result = speak.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    if (result == TextToSpeech.ERROR)
                        Toast.makeText(context, "Speech failure", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Fail to start voice assistance", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            int result = speak.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            if (result == TextToSpeech.ERROR)
                Toast.makeText(context, "Speech failure", Toast.LENGTH_SHORT).show();
        }
    }
}
