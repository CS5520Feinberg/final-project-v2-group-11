package com.example.numad23su_gourpv2_11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StickItToEm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        Button loginButton = findViewById(R.id.loginBtn);
        EditText username = findViewById(R.id.Username);

        loginButton.setOnClickListener(view -> loginButtonClicked());
    }

    public void loginButtonClicked () {
        Intent intent = new Intent(StickItToEm.this , LoginButtonClicked.class);
        startActivity(intent);
    }
}