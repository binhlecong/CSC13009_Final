package com.hcmus.csc13009.smartenglish.frontend.entrance;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hcmus.csc13009.smartenglish.detection.R;
import com.hcmus.csc13009.smartenglish.detection.databinding.ActivityEntranceBinding;
import com.hcmus.csc13009.smartenglish.frontend.entrance.fragment.OnboaringIntroductionFragment;
import com.hcmus.csc13009.smartenglish.frontend.entrance.fragment.OnboaringWelcomeFragment;

public class EntranceActivity extends AppCompatActivity {

    protected ActivityEntranceBinding binding;
    private static final int NUM_PAGES = 2;
    private ViewPager viewPager;
    private ScreenSliderPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_entrance);

        viewPager = findViewById(R.id.liquid_pager);
        pagerAdapter = new ScreenSliderPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    private class ScreenSliderPagerAdapter extends FragmentStatePagerAdapter{


        public ScreenSliderPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    OnboaringWelcomeFragment onboaringWelcomeFragment = new OnboaringWelcomeFragment();
                    return onboaringWelcomeFragment;
                case 1:
                    OnboaringIntroductionFragment onboaringIntroductionFragment = new OnboaringIntroductionFragment();
                    return onboaringIntroductionFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
