package com.example.verkli.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.verkli.R;
import com.example.verkli.ui.BaseActivity;
import com.example.verkli.ui.SearchActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Set;

import com.example.verkli.model.Track;

public interface SearchFunctions {

    static void search(Set<Track> tracks, Activity SearchActivity, String word, FirebaseFirestore db, LinearLayout resultLayout, String searchFor) {
        db.collection("tracks").orderBy(searchFor).startAt(word).endAt(word + "\uf8ff").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                String title = document.getString("title");
                String artist = document.getString("artist");
                String album = document.getString("album");
                String genre = document.getString("genre");
                String streamUrl = document.getString("streamUrl");

                int streamCount;
                try {
                    streamCount = document.getLong("streamCnt").intValue();
                } catch (NullPointerException e) {
                    streamCount = 0;
                }

                int duration = 100;
                try {
                    duration = document.getLong("duration").intValue();
                } catch (NullPointerException e) {
                    duration = 100;
                }

                tracks.add(new Track(title, duration, streamCount, streamUrl, genre, album, artist));
            }

            for (Track track : tracks) {
                LayoutInflater li = LayoutInflater.from(SearchActivity.getBaseContext());
                View trackView = li.inflate(R.layout.result_item, resultLayout, false);

                TextView trackTitle = trackView.findViewById(R.id.title);
                TextView trackArtist = trackView.findViewById(R.id.artist);
                TextView trackAlbum = trackView.findViewById(R.id.album);

                trackTitle.setText(track.getTitle());
                trackArtist.setText(track.getArtist());
                trackAlbum.setText(track.getAlbum());

                trackView.setOnClickListener(v -> {
                    db.collection("tracks").whereEqualTo("title", track.getTitle()).whereEqualTo("artist", track.getArtist()).get().addOnSuccessListener(documentSnapshot -> {
                        for (DocumentSnapshot document : documentSnapshot) {
                            document.getReference().update("streamCnt", FieldValue.increment(1));
                        }
                    });

                    BaseActivity.setCurrentTrack(track);
                });

                resultLayout.addView(trackView);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(SearchActivity.getBaseContext(), "Sikertelen keresés!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        });
    }
}
