package com.hcmus.csc13009.smartenglish.frontend.listwords.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hcmus.csc13009.smartenglish.detection.databinding.FragmentListWordsCorrectBinding;
import com.hcmus.csc13009.smartenglish.frontend.listwords.ListWordsViewModel;
import com.hcmus.csc13009.smartenglish.frontend.listwords.adapter.CorrectWordAdapter;

public class ListWordsCorrectFragment extends Fragment {

    protected FragmentListWordsCorrectBinding binding;
    private ListWordsViewModel viewModel;
    private CorrectWordAdapter adapter;

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

        viewModel = new ViewModelProvider(this).get(ListWordsViewModel.class);
        initRecyclerView();

    }

    private void initRecyclerView() {

        adapter = new CorrectWordAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);

        viewModel.getAllCorrectWords().observe(getViewLifecycleOwner(), words -> {
            if (words != null) {
                adapter.setWords(words);
            }
        });


    }

}
