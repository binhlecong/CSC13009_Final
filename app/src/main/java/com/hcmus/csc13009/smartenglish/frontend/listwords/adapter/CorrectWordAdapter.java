package com.hcmus.csc13009.smartenglish.frontend.listwords.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.csc13009.smartenglish.data.model.Word;
import com.hcmus.csc13009.smartenglish.detection.databinding.ItemListWordsCorrectBinding;

import java.util.List;

public class CorrectWordAdapter extends RecyclerView.Adapter<CorrectWordAdapter.CorrectWordViewHolder>{

    private ItemListWordsCorrectBinding binding;
    private List<Word> allWords;

    public CorrectWordAdapter() {
    }

    @NonNull
    @Override
    public CorrectWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemListWordsCorrectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CorrectWordAdapter.CorrectWordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CorrectWordViewHolder holder, int position) {
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

    public static class CorrectWordViewHolder extends RecyclerView.ViewHolder{

        private ItemListWordsCorrectBinding binding;

        public CorrectWordViewHolder(@NonNull ItemListWordsCorrectBinding itemBinding) {
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
