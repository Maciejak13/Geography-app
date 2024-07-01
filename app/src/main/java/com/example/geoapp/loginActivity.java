package com.example.geoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail, editTextPassword;
    private Button btnLogin, btnReg, btnResetPassword;
    private CheckBox check;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        check = findViewById(R.id.remember_me_chck);
        btnReg = findViewById(R.id.btnReg);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        check.setChecked(rememberMe);

        if (rememberMe) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");
            editTextEmail.setText(savedEmail);
            editTextPassword.setText(savedPassword);
        }

        swichReg();
        swichReg();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        boolean rememberMe = check.isChecked();
        if (rememberMe) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", true);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "Logowanie udane", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(loginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(loginActivity.this, "Logowanie nieudane", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void swichReg(){
        Button quizButton = findViewById(R.id.btnReg);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, registerActivity.class));
            }
        });
    }

    private void resetPassword() {
        String email = editTextEmail.getText().toString().trim();

        if (!email.isEmpty()) {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(loginActivity.this, "Wysłano link resetujący hasło na podany adres e-mail", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(loginActivity.this, "Nie udało się wysłać linku resetującego hasło. Spróbuj ponownie.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(loginActivity.this, "Wprowadź poprawny adres e-mail", Toast.LENGTH_SHORT).show();
        }
    }
}
