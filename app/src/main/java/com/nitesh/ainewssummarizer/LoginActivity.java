package com.nitesh.ainewssummarizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etEmail, etPassword;
    Button btnLogin;
    TextView txtRegister;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if already logged in
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if(isLoggedIn){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                return;

            }

            boolean check = db.checkUser(email,password);

            if(check){

                Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show();

                // Save login session
                SharedPreferences prefs1 = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs1.edit();

                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                startActivity(new Intent(this, MainActivity.class));
                finish();

            }else{

                Toast.makeText(this,"Wrong Email or Password",Toast.LENGTH_SHORT).show();

            }

        });

        txtRegister.setOnClickListener(v -> {

            startActivity(new Intent(this, RegisterActivity.class));

        });

    }
}