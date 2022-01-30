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
import com.hcmus.csc13009.smartenglish.frontend.listwords.adapter.RecentWordAdapter;

public class ListWordsRecentFragment extends Fragment {

    protected FragmentListWordsCorrectBinding binding;
    private ListWordsViewModel viewModel;
    private RecentWordAdapter adapter;

    public ListWordsRecentFragment(){}

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

        adapter = new RecentWordAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);

        viewModel.getAllWords().observe(getViewLifecycleOwner(), words -> {
            if (words != null) {
                adapter.setWords(words);
            }
        });
    }

}
