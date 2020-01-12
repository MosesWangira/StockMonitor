package com.example.android.stockMonitor;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
/*
 *created by moses
 */

public class Usermanual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanual);

        LinearLayout usermanual = (LinearLayout)findViewById(R.id.usermanually);

        ArrayList<String> assist = new ArrayList<>();

        assist.add("USER MANUAL");
        assist.add("\n");
        assist.add("Add item");
        assist.add("Click on the button on left bottom to add item");
        assist.add("\n");
        assist.add("Edit Item");
        assist.add("In case you want to update stock click on item to update and update");
        assist.add("Change Language");
        assist.add("The app supports french swahili and english you can change the phone language and it will support the languages");


        for (int i=0 ; i<assist.size() ; i++)
        {
            TextView listView = new TextView(this);
            listView.setTextColor(Color.BLACK);
            listView.setGravity(Gravity.LEFT);
            listView.setText(assist.get(i));
            usermanual.addView(listView);
        }

    }

}

