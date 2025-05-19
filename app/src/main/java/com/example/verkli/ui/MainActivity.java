package com.example.verkli.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.verkli.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_main);
        TextView home = findViewById(R.id.home);
        home.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        FirebaseApp.initializeApp(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        BaseActivity.setCurrentTrack(BaseActivity.getCurrentTrack());
    }


}