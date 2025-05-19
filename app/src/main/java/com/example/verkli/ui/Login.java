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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.verkli.R;
import com.example.verkli.utils.PasswordCharSequence;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Login extends AppCompatActivity {

    private EditText emailAddress;
    private EditText password;
    private Button loginButton;

    private FirebaseAuth mAuth;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(com.example.verkli.R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);


        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        emailAddress.startAnimation(slideIn);
        password.startAnimation(slideIn);
        loginButton.startAnimation(slideIn);
        registerLink.startAnimation(slideIn);

        password.setTransformationMethod(new PasswordTransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence charSequence, View view) {
                return new PasswordCharSequence(charSequence);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailAddress.getText().toString().trim();
                String pwd = password.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(Login.this, "Please fill in the blank fields!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                emailAddress.startAnimation(slideOut);
                                password.startAnimation(slideOut);
                                loginButton.startAnimation(slideOut);
                                registerLink.startAnimation(slideOut);

                                Intent main = new Intent(Login.this, MainActivity.class);
                                startActivity(main);
                                finish();
                            } else {
                                Exception e = task.getException();
                                if (e instanceof FirebaseAuthInvalidUserException) {
                                    Toast.makeText(Login.this, "User not found with this email!", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(Login.this, "This password is incorrect!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, "Login failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }


}