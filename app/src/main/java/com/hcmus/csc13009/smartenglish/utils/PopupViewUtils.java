package com.hcmus.csc13009.smartenglish.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcmus.csc13009.smartenglish.data.api.pojos.Example;
import com.hcmus.csc13009.smartenglish.detection.R;

import java.util.ArrayList;

public class PopupViewUtils {
    public static void showPopupWindow(final View view, ArrayList<Example> definition) {
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

        //Initialize the elements of our window, install the handler
        TextView test2 = popupView.findViewById(R.id.titleText);
        test2.setText(definition.get(0).getWord());


        FloatingActionButton closePopup = popupView.findViewById(R.id.close_popup);
        //Handler for clicking on the inactive zone of the window
        closePopup.setOnClickListener(v -> popupWindow.dismiss());
    }
}
