package com.example.verkli.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.verkli.model.Track;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.verkli.R;

public class UploadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_upload);
        TextView upload = findViewById(R.id.upload);
        upload.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        FirebaseApp.initializeApp(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText titleInput = findViewById(R.id.titleInput);
        EditText artistInput = findViewById(R.id.artistInput);
        EditText albumInput = findViewById(R.id.albumInput);
        EditText genreInput = findViewById(R.id.genreInput);
        CheckBox checkbox = findViewById(R.id.checkbox);

        Button uploadButton = findViewById(R.id.uploadButton);
        
        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                db.collection("users").whereEqualTo("email", user.getEmail()).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.isEmpty()) {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                    } else {
                        artistInput.setText(documentSnapshot.getDocuments().get(0).getString("username"));
                    }
                });
            } else {
                artistInput.setText("");
            }
        });
        
        uploadButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String artist = artistInput.getText().toString().trim();
            String album = albumInput.getText().toString().trim();
            String genre = genreInput.getText().toString().trim();

            if (title.isEmpty() || artist.isEmpty() || album.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            } else {
                Track track = new Track(title, 100, 0, "url", genre, album, artist);
                db.collection("tracks").add(track).addOnSuccessListener(
                        documentReference -> {
                            Toast.makeText(this, "Upload successful!", Toast.LENGTH_SHORT).show();
                        }
                );

                titleInput.setText("");
                artistInput.setText("");
                albumInput.setText("");
                genreInput.setText("");
            }
        });
    }
}