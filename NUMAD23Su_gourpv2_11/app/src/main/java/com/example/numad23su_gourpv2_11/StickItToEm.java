package com.example.numad23su_gourpv2_11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StickItToEm extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button loginButton = findViewById(R.id.loginBtn);
        EditText usernameField = findViewById(R.id.Username);

        loginButton.setOnClickListener(view -> {
            String username = usernameField.getText().toString();
            if (username.isEmpty()) {
                Toast.makeText(StickItToEm.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            } else {
                login(username);
            }
        });
    }

    public void login (String username) {
        mDatabase.child("users").child(username).setValue(true);

        // Save username in shared preferences
        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.apply();

        // Proceed to next activity
        Intent intent = new Intent(StickItToEm.this, LoginButtonClicked.class);
        startActivity(intent);
    }
}