package com.example.verkli.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.verkli.utils.PasswordCharSequence;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.verkli.R;

import java.util.Objects;

public class ProfileActivity extends BaseActivity {
    //private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_profile);
        TextView profile = findViewById(R.id.profile);
        profile.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        FirebaseApp.initializeApp(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ImageView profilePic = findViewById(R.id.profilePic);
        EditText changeUsername = findViewById(R.id.changeUsername);
        EditText changePassword = findViewById(R.id.changePassword);
        Button logoutButton = findViewById(R.id.logoutButton);
        //Button deleteButton = findViewById(R.id.deleteButton);

        changePassword.setTransformationMethod(new PasswordTransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence charSequence, View view) {
                return new PasswordCharSequence(charSequence);
            }
        });

        profilePic.setOnClickListener(v -> {
            
        });

        changeUsername.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && event != null) {
                String newUsername = changeUsername.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;

                db.collection("users").whereEqualTo("email", user.getEmail()).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.isEmpty()) {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                    } else {
                        DocumentSnapshot snapshot = documentSnapshot.getDocuments().get(0);
                        DocumentReference userRef = snapshot.getReference();
                        userRef.update("username", newUsername);
                        Toast.makeText(this, "Username change successful!", Toast.LENGTH_SHORT).show();
                        changeUsername.setText("");
                    }
                });
                return true;
            }
            return false;
        });

        changePassword.setOnClickListener(v -> {
            String newPassword = changePassword.getText().toString();
            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updatePassword(newPassword).addOnSuccessListener(
                    aVoid -> {
                        changePassword.setText("");
                        Toast.makeText(ProfileActivity.this, "Password change successful!", Toast.LENGTH_SHORT).show();
                    }
            );
        });

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, Login.class);
            startActivity(intent);
            finish();
        });
    }
}