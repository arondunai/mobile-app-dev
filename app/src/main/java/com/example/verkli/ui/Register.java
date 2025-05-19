package com.example.verkli.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.verkli.R;
import com.example.verkli.utils.PasswordCharSequence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText userName;
    private EditText emailAddress;
    private EditText password;
    private EditText confirmPassword;
    private Button registButton;
    private TextView LoginLink;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        userName = findViewById(R.id.userName);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        registButton = findViewById(R.id.registButton);
        LoginLink = findViewById(R.id.loginLink);

        userName.startAnimation(slideIn);
        emailAddress.startAnimation(slideIn);
        password.startAnimation(slideIn);
        confirmPassword.startAnimation(slideIn);
        registButton.startAnimation(slideIn);

        password.setTransformationMethod(new PasswordTransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence charSequence, View view) {
                return new PasswordCharSequence(charSequence);
            }
        });

        confirmPassword.setTransformationMethod(new PasswordTransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence charSequence, View view) {
                return new PasswordCharSequence(charSequence);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getText().toString().trim();
                String email = emailAddress.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                String cnfrmPwd = confirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(cnfrmPwd)) {
                     Toast.makeText(Register.this, "Please fill in the blank fields!", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(cnfrmPwd)) {
                    Toast.makeText(Register.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                } else if (pwd.length() < 6) {
                    Toast.makeText(Register.this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(task -> {
                        FirebaseUser user = mAuth.getCurrentUser();

                        Map<String, String> userData = new HashMap<>();
                        userData.put("username", username);
                        userData.put("email", email);

                        if (user == null) throw new AssertionError();

                        db.collection("users").document(user.getUid()).set(userData).addOnSuccessListener(
                                aVoid -> {
                                    Toast.makeText(Register.this, "User created!", Toast.LENGTH_SHORT).show();
                                }
                        );
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                            userName.startAnimation(slideOut);
                            emailAddress.startAnimation(slideOut);
                            password.startAnimation(slideOut);
                            confirmPassword.startAnimation(slideOut);
                            registButton.startAnimation(slideOut);

                            Intent main = new Intent(Register.this, MainActivity.class);
                            startActivity(main);
                            finish();
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Register.this, "This email is already in use!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Register.this, "Registration failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }});
                }
            }
        });

        LoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }
}