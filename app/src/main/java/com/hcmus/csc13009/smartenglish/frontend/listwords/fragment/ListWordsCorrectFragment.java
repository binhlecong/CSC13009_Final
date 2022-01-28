package com.hcmus.csc13009.smartenglish.frontend.listwords.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcmus.csc13009.smartenglish.detection.R;

public class ListWordsCorrectFragment extends Fragment {

    public ListWordsCorrectFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_words_correct, container, false);
        return rootView;
    }
}
