package com.hcmus.csc13009.smartenglish.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcmus.csc13009.smartenglish.detection.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PopupView {
    Context context;
    View popupView;

    public PopupView(Context context) {
        this.context = context;
    }

    public void showPopupWindow(final View view, String word) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(
                view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.word_detail_page, null);
        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        // Display words in a horizontal listview
        TextView titleWord = popupView.findViewById(R.id.title_word);
        titleWord.setText(word);
        // Add close btn
        FloatingActionButton closePopup = popupView.findViewById(R.id.close_popup);
        //Handler for clicking on the inactive zone of the window
        closePopup.setOnClickListener(v -> popupWindow.dismiss());

//        // Call api
//        DictionaryApi dictionaryApi = ApiUtils.getRetrofitClient();
//        dictionaryApi.getExample("run").enqueue(new Callback<ArrayList<Example>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Example>> call,
//                                   Response<ArrayList<Example>> response) {
//                // TODO
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Example>> call, Throwable t) {
//                Toast.makeText(context, "Fail to load words",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
        new ParseUrl().execute(ParserUtils.url + word);
    }

    private void showProgressCircular() {
        ProgressBar progressBar = popupView.findViewById(R.id.progress_circular);
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressCircular() {
        ProgressBar progressBar = popupView.findViewById(R.id.progress_circular);
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    private void showDefinition(String s) {
        TextView content = popupView.findViewById(R.id.content);
        if (content != null) content.setText(s);
    }

    private class ParseUrl extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressCircular();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buffer = new StringBuffer();

            try {
                Document document = Jsoup.connect(params[0]).get();
                if (document == null) return "Fail to get document";
                Element content = document.getElementsByClass("content").get(0);
                Elements divs = content.getElementsByTag("div");
                for (int i = 1; i < divs.size(); i++) {
                    buffer.append(divs.get(i).text()).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideProgressCircular();
            showDefinition(s);
        }
    }

}
