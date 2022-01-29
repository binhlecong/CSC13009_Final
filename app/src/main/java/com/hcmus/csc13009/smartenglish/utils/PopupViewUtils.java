package com.hcmus.csc13009.smartenglish.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcmus.csc13009.smartenglish.detection.R;

public class PopupViewUtils {
    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater =
                (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.word_detail_page, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        TextView test2 = popupView.findViewById(R.id.titleText);
        test2.setText("whbfwsbfbfs");

        Button buttonEdit = popupView.findViewById(R.id.messageButton);
        buttonEdit.setOnClickListener(v -> {
            //As an example, display the message
            Toast.makeText(view.getContext(), "Wow, popup action button", Toast.LENGTH_SHORT).show();
        });

        FloatingActionButton closePopup = popupView.findViewById(R.id.close_popup);
        //Handler for clicking on the inactive zone of the window
        closePopup.setOnClickListener(v -> popupWindow.dismiss());
    }
}
