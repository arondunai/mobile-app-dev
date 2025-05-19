package com.example.verkli.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.verkli.R;
import com.example.verkli.model.Track;

public class BaseActivity extends AppCompatActivity {

    protected static Track currentTrack = null;
    protected static Activity currentActivity = null;

    public static Track getCurrentTrack() {
        return currentTrack;
    }

    public static void setCurrentTrack(Track currentTrack) {
        BaseActivity.currentTrack = currentTrack;

        if (currentActivity instanceof BaseActivity) {
            TextView trackTitle = currentActivity.findViewById(R.id.trackTitle);
            TextView trackArtist = currentActivity.findViewById(R.id.trackArtist);

            if (trackTitle != null && trackArtist != null) {
                trackTitle.setText(currentTrack.getTitle());
                trackArtist.setText(currentTrack.getArtist());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;

        if (currentTrack != null) {
            TextView trackTitle = findViewById(R.id.trackTitle);
            TextView trackArtist = findViewById(R.id.trackArtist);

            if (trackTitle != null && trackArtist != null) {
                trackTitle.setText(currentTrack.getTitle());
                trackArtist.setText(currentTrack.getArtist());
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setContentLayout(int layoutResID) {
        ViewGroup base = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_base, null);
        getLayoutInflater().inflate(layoutResID, base.findViewById(R.id.baseContent), true);
        setContentView(base);

        TextView home = findViewById(R.id.home);
        TextView search = findViewById(R.id.search);
        TextView upload = findViewById(R.id.upload);
        TextView profile = findViewById(R.id.profile);

        home.setOnClickListener(v -> {
            if (!(this instanceof MainActivity)) {
                startActivity(new Intent(this, MainActivity.class));
            }
        });

        search.setOnClickListener(v -> {
            if (!(this instanceof SearchActivity)) {
                startActivity(new Intent(this, SearchActivity.class));
            }
        });

        upload.setOnClickListener(v -> {
            if (!(this instanceof UploadActivity)) {
                startActivity(new Intent(this, UploadActivity.class));
            }
        });

        profile.setOnClickListener(v -> {
            if (!(this instanceof ProfileActivity)) {
                startActivity(new Intent(this, ProfileActivity.class));
            }
        });
    }
}
