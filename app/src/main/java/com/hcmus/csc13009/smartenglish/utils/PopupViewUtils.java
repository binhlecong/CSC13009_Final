package com.hcmus.csc13009.smartenglish.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcmus.csc13009.smartenglish.data.api.ApiUtils;
import com.hcmus.csc13009.smartenglish.data.api.DictionaryApi;
import com.hcmus.csc13009.smartenglish.data.api.pojos.Example;
import com.hcmus.csc13009.smartenglish.detection.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopupViewUtils {
    public static void showPopupWindow(Context context, final View view, String[] words) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(
                view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.word_detail_page, null);
        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        // TODO: display words in a horizontal listview, press on each word -> the call api
        LinearLayout wordsList = popupView.findViewById(R.id.words_list);
        populateTitleWords(context, wordsList, words);


        FloatingActionButton closePopup = popupView.findViewById(R.id.close_popup);
        //Handler for clicking on the inactive zone of the window
        closePopup.setOnClickListener(v -> popupWindow.dismiss());

        // Call api
        DictionaryApi dictionaryApi = ApiUtils.getRetrofitClient();
        dictionaryApi.getExample("run").enqueue(new Callback<ArrayList<Example>>() {
            @Override
            public void onResponse(Call<ArrayList<Example>> call,
                                   Response<ArrayList<Example>> response) {
                // TODO
            }

            @Override
            public void onFailure(Call<ArrayList<Example>> call, Throwable t) {
                Toast.makeText(context, "Fail to load words",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void populateTitleWords(Context context, LinearLayout linearLayout,
                                           String[] words) {
        for (String word : words) {
            TextView wordView = new TextView(context);
            wordView.setText(word);
            wordView.setTextSize((float) 24);
            wordView.setTextColor(0xffffa800);
            wordView.setPadding(5, 5, 5, 5);
            linearLayout.addView(wordView);
        }
    }
}
