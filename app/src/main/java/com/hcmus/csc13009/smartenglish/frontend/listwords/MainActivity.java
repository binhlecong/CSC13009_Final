package com.hcmus.csc13009.smartenglish.frontend.listwords;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.hcmus.csc13009.smartenglish.detection.camera.DetectorActivity;
import com.hcmus.csc13009.smartenglish.detection.databinding.ActivityMainBinding;
import com.hcmus.csc13009.smartenglish.detection.R;
import com.hcmus.csc13009.smartenglish.frontend.listwords.fragment.ListWordsCorrectFragment;
import com.hcmus.csc13009.smartenglish.frontend.listwords.fragment.ListWordsIncorrectFragment;
import com.hcmus.csc13009.smartenglish.frontend.listwords.fragment.ListWordsRecentFragment;
import com.hcmus.csc13009.smartenglish.frontend.utils.BottomBarBehavior;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;

    Fragment fragment = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavigationBar();

        initSpeedDialFloatingButton(savedInstanceState == null);

    }

    private void initNavigationBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        BubbleNavigationLinearView navigationBar = findViewById(R.id.navigationBar);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigationBar.getLayoutParams();
        layoutParams.setBehavior(new BottomBarBehavior());

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ListWordsRecentFragment()).commit();

        navigationBar.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position) {
                    case 1:
                        fragment = new ListWordsCorrectFragment();
                        break;
                    case 2:
                        fragment = new ListWordsIncorrectFragment();
                        break;
                    default:
                        fragment = new ListWordsRecentFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        });
    }

    private void initSpeedDialFloatingButton(boolean isAddActionItems) {
        SpeedDialView speedDialView = findViewById(R.id.speedDial);
        if (isAddActionItems){
            speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_study, R.drawable.ic_study)
                    .setLabel("Study Mode")
                    .create());
            speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_exam, R.drawable.ic_exam)
                    .setLabel("Exam Mode")
                    .create());
        }

        speedDialView.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()) {
                case R.id.fab_study:
                    Intent studyIntent = new Intent(this, DetectorActivity.class);
                    studyIntent.putExtra("mode", "study");
                    startActivity(studyIntent);
                    break;
                case R.id.fab_exam:
                    Intent examIntent = new Intent(this, DetectorActivity.class);
                    examIntent.putExtra("mode", "exam");
                    startActivity(examIntent);
                    break;
                default:
                    return false;
            }
            return false;
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
