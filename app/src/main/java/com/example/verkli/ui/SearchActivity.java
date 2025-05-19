package com.example.verkli.ui;

import static com.example.verkli.model.SearchFunctions.search;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.verkli.R;
import com.example.verkli.model.Track;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class SearchActivity extends BaseActivity {

    private EditText searchBar;
    private LinearLayout resultLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_search);
        TextView search = findViewById(R.id.search);
        search.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        FirebaseApp.initializeApp(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        searchBar = findViewById(R.id.searchBar);
        resultLayout = findViewById(R.id.resultLayout);

        searchBar.setOnTouchListener((v, event) -> {
            resultLayout.removeAllViews();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndWidth = searchBar.getCompoundDrawables()[2] != null
                        ? searchBar.getCompoundDrawables()[2].getBounds().width()
                        : 0;

                if (event.getX() >= (searchBar.getWidth() - searchBar.getPaddingEnd() - drawableEndWidth)) {
                    String[] searchQuery = searchBar.getText().toString().trim().split(" ");
                    if (searchQuery.length != 0) {
                        Set<Track> tracks = new HashSet<>();
                        for (String word : searchQuery) {
                            search(tracks, SearchActivity.this, word, db, resultLayout, "title");
                            search(tracks, SearchActivity.this, word, db, resultLayout, "artist");
                            search(tracks, SearchActivity.this, word, db, resultLayout, "album");
                        }
                    }
                }
            }
            return false;
        });
    }
}
