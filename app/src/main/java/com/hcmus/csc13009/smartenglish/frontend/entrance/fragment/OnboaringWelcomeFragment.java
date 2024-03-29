package com.hcmus.csc13009.smartenglish.frontend.entrance.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcmus.csc13009.smartenglish.detection.databinding.FragmentOnboaringIntroductionBinding;
import com.hcmus.csc13009.smartenglish.detection.databinding.FragmentOnboaringWelcomeBinding;

public class OnboaringWelcomeFragment extends Fragment {

    protected FragmentOnboaringWelcomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboaringWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
