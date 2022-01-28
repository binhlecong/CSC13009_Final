package com.hcmus.csc13009.smartenglish.frontend.listwords.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcmus.csc13009.smartenglish.detection.R;
import com.hcmus.csc13009.smartenglish.detection.camera.DetectorActivity;
import com.hcmus.csc13009.smartenglish.detection.databinding.FragmentListWordsCorrectBinding;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

public class ListWordsCorrectFragment extends Fragment {

    protected FragmentListWordsCorrectBinding binding;

    public ListWordsCorrectFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListWordsCorrectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSpeedDialFloatingButton(savedInstanceState == null);

    }

    private void initSpeedDialFloatingButton(boolean isAddActionItems) {
        SpeedDialView speedDialView = binding.speedDial;
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
                    Intent studyIntent = new Intent(requireContext(), DetectorActivity.class);
                    startActivity(studyIntent);
                    break;
                case R.id.fab_exam:
                    Intent examIntent = new Intent(requireContext(), DetectorActivity.class);
                    startActivity(examIntent);
                    break;
                default:
                    return false;
            }
            return false;
        });
    }
}
