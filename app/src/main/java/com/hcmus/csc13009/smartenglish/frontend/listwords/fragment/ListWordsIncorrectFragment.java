package com.hcmus.csc13009.smartenglish.frontend.listwords.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hcmus.csc13009.smartenglish.detection.databinding.FragmentListWordsIncorrectBinding;
import com.hcmus.csc13009.smartenglish.frontend.listwords.ListWordsViewModel;
import com.hcmus.csc13009.smartenglish.frontend.listwords.adapter.IncorrectWordAdapter;

public class ListWordsIncorrectFragment extends Fragment {

    protected FragmentListWordsIncorrectBinding binding;
    private ListWordsViewModel viewModel;
    private IncorrectWordAdapter adapter;

    public ListWordsIncorrectFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListWordsIncorrectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ListWordsViewModel.class);
        initRecyclerView();

    }

    private void initRecyclerView() {

        adapter = new IncorrectWordAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);

        viewModel.getAllInCorrectWords().observe(getViewLifecycleOwner(), words -> {
            if (words != null) {
                Log.i("@LW", "List words data changed, size = " + words.size());
                adapter.setWords(words);
            }
        });


    }


}
