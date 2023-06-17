package com.example.languagelearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    TextView textViewPassword;
    Button btnLogin;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    ImageButton btnBack;
    ImageView passwordToggle;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        progressBar = findViewById(R.id.prog_bar);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        textViewPassword = findViewById(R.id.textViewPasswordPrompt);
        btnLogin = findViewById(R.id.btn_login);
        btnBack = findViewById(R.id.imageBack);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                progressBar.setVisibility(view.VISIBLE);

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(view.GONE);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Login.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(view.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(view.GONE);
                    return;

                }
                if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    textViewPassword.setText("Password must contain atleast 1 number, small and big letter, symbol, " +
                            "and at least 6 characters");
                    Toast.makeText(Login.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(view.GONE);
                    return;
                }

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && PASSWORD_PATTERN.matcher(password).matches()) {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(view.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(Login.this, "Log-in Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Login.this, "Log-in failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FrontPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}