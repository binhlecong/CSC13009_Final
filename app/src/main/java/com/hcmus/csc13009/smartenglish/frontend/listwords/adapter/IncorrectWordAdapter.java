package com.hcmus.csc13009.smartenglish.frontend.listwords.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.csc13009.smartenglish.data.model.Word;
import com.hcmus.csc13009.smartenglish.detection.databinding.ItemListWordsCorrectBinding;
import com.hcmus.csc13009.smartenglish.detection.databinding.ItemListWordsIncorrectBinding;

import java.util.List;

public class IncorrectWordAdapter extends RecyclerView.Adapter<IncorrectWordAdapter.IncorrectWordViewHolder>{

    private ItemListWordsIncorrectBinding binding;
    private List<Word> allWords;

    public IncorrectWordAdapter() {
    }

    @NonNull
    @Override
    public IncorrectWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemListWordsIncorrectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new IncorrectWordAdapter.IncorrectWordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IncorrectWordViewHolder holder, int position) {
        Word word = allWords.get(position);
        binding.word.setText(word.getWord());
        String correctPerTotal = word.getCorrect() + "/" +word.getTotal();
        binding.correctPerTotal.setText(correctPerTotal);
        holder.setImage(word.getWord());
    }

    @Override
    public int getItemCount() {
        if (allWords != null)
            return allWords.size();
        return 0;
    }

    public void setWords(List<Word> listWords) {
        if (listWords != null) {
            this.allWords = listWords;
            notifyDataSetChanged();
        }
    }

    public static class IncorrectWordViewHolder extends RecyclerView.ViewHolder{

        private ItemListWordsIncorrectBinding binding;

        public IncorrectWordViewHolder(@NonNull ItemListWordsIncorrectBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }

        public void setImage(String label) {
            if (label.equals("television"))
                label = "tv";
            label = label.replaceAll("\\s+", "").toLowerCase();
            Context context = binding.getRoot().getContext();
            int resId = context.getResources().getIdentifier("word_" + label, "drawable", context.getPackageName());
            if (resId == 0)
                return;
            binding.imageCorrectListItem.setImageResource(resId);
        }
    }
}
