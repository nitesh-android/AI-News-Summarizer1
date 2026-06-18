package com.nitesh.ainewssummarizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etEmail, etPassword;
    Button btnRegister;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        db = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Empty check
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                return;

            }

            boolean inserted = db.insertUser(email, password);

            if(inserted){

                Toast.makeText(this,"Account Created Successfully",Toast.LENGTH_SHORT).show();

                // Go to Login page
                startActivity(new Intent(this, LoginActivity.class));
                finish();

            }else{

                Toast.makeText(this,"User already exists",Toast.LENGTH_SHORT).show();

            }

        });

    }
}