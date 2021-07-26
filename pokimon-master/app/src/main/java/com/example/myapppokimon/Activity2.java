package com.example.myapppokimon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        String value = getIntent().getExtras().getString("key");

        TextView textView = (TextView) findViewById(R.id.received);
        textView.setText(value);
    }


}