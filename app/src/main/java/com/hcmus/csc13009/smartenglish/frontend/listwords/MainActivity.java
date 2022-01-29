package com.hcmus.csc13009.smartenglish.frontend.listwords;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.hcmus.csc13009.smartenglish.detection.databinding.ActivityMainBinding;
import com.hcmus.csc13009.smartenglish.detection.R;
import com.hcmus.csc13009.smartenglish.frontend.listwords.fragment.ListWordsCorrectFragment;
import com.hcmus.csc13009.smartenglish.frontend.listwords.fragment.ListWordsIncorrectFragment;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;

    Fragment fragment = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BubbleNavigationLinearView navigationBar = findViewById(R.id.navigationBar);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ListWordsCorrectFragment()).commit();

        navigationBar.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position) {
                    case 0:
                        fragment = new ListWordsCorrectFragment();
                        break;
                    case 1:
                        fragment = new ListWordsIncorrectFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        });

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }
}
