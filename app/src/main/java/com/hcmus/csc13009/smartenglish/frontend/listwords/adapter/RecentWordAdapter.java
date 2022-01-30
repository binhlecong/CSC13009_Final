package com.hcmus.csc13009.smartenglish.frontend.listwords.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.csc13009.smartenglish.data.model.Word;
import com.hcmus.csc13009.smartenglish.detection.databinding.ItemListWordsRecentBinding;
import com.hcmus.csc13009.smartenglish.utils.PopupView;
import com.hcmus.csc13009.smartenglish.utils.TextToSpeechUtils;

import java.util.List;

public class RecentWordAdapter extends RecyclerView.Adapter<RecentWordAdapter.RecentWordViewHolder> {

    private ItemListWordsRecentBinding binding;
    private List<Word> allWords;

    public RecentWordAdapter() {
    }

    @NonNull
    @Override
    public RecentWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemListWordsRecentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecentWordAdapter.RecentWordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentWordViewHolder holder, int position) {
        Word word = allWords.get(position);
        binding.word.setText(word.getWord());
        holder.setImage(word.getWord());
        holder.setSpeaker(word.getWord());
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

    public static class RecentWordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final private ItemListWordsRecentBinding binding;

        public RecentWordViewHolder(@NonNull ItemListWordsRecentBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
            binding.getRoot().setOnClickListener(this);
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

        public void setSpeaker(String label) {
            Context context = binding.getRoot().getContext();
            binding.wordSpeakerImage.setOnClickListener(view -> TextToSpeechUtils.speak(context, label));
        }

        @Override
        public void onClick(View view) {
            PopupView popupView = new PopupView(view.getContext());
            popupView.showPopupWindow(view, binding.word.getText().toString());
        }
    }
}
